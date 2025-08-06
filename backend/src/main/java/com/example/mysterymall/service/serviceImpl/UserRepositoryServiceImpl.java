package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.CartRepository;
import com.example.mysterymall.dao.CommentRepository;
import com.example.mysterymall.dao.MysteryBoxItemRepository;
import com.example.mysterymall.dao.ProductRepository;
import com.example.mysterymall.dao.UserRepositoryRepository;
import com.example.mysterymall.po.CartItem;
import com.example.mysterymall.po.Comment;
import com.example.mysterymall.po.MysteryBoxItem;
import com.example.mysterymall.po.Product;
import com.example.mysterymall.po.UserRepository;
import com.example.mysterymall.service.CartService;
import com.example.mysterymall.service.UserRepositoryService;
import com.example.mysterymall.vo.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserRepositoryServiceImpl implements UserRepositoryService {

    @Resource
    private UserRepositoryRepository userRepositoryRepository;

    @Resource
    private CartService cartService;

    @Resource
    private CartRepository cartRepository;

    @Resource
    private ProductRepository productRepository;

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private MysteryBoxItemRepository mysteryBoxItemRepository;

    @Override
    public Response<List<UserRepository>> getUserPurchases(String username) {
        List<UserRepository> purchases = userRepositoryRepository.findByUsername(username);
        return Response.buildSuccess(purchases);
    }

    @Override
    @Transactional
    public Response<String> purchaseFromCart(String username, String address, String contactPhone) {
        // 1. 获取用户选中的购物车商品
        Response<List<CartItem>> selectedItemsResponse = cartService.getSelectedCartItems(username);
        List<CartItem> selectedItems = selectedItemsResponse.getData();

        if (selectedItems == null || selectedItems.isEmpty()) {
            return Response.buildFailure("购物车中没有选中的商品", "400");
        }

        // 2. 生成唯一的订单ID
        Long orderId = generateUniqueOrderId();

        // 3. 检查库存并转移到用户仓库
        List<Long> successfullyPurchasedProductIds = new ArrayList<>();

        for (CartItem item : selectedItems) {
            Long productId = item.getProductId();
            Integer quantity = item.getQuantity();

            // 检查商品和库存
            Optional<Product> productOptional = productRepository.findById(productId);
            if (!productOptional.isPresent()) {
                continue; // 商品不存在，跳过
            }

            Product product = productOptional.get();
            if (product.getStock() < quantity) {
                continue; // 库存不足，跳过
            }

            // 更新库存
            product.setStock(product.getStock() - quantity);
            productRepository.save(product);

            // 检查商品是否是盲盒类型
            boolean isBlindBox = "盲盒".equals(product.getCategory());

            // 如果是盲盒商品，每个盲盒单独一条记录，不合并
            if (isBlindBox) {
                // 每个盲盒单独创建一条记录，方便后续单独抽取
                for (int i = 0; i < quantity; i++) {
                    UserRepository userRepository = new UserRepository();
                    userRepository.setUsername(username);
                    userRepository.setProductId(productId);
                    userRepository.setProductName(product.getName());
                    userRepository.setProductImage(product.getImage());
                    userRepository.setQuantity(1); // 每条记录只有一个盲盒
                    userRepository.setPrice(product.getPrice());
                    userRepository.setOrderId(orderId);
                    userRepository.setPurchaseTime(LocalDateTime.now());
                    userRepository.setStatus("已付款");
                    userRepository.setIsRated(false);
                    // 标记为盲盒商品
                    userRepository.setRemark("盲盒商品，可抽取");

                    userRepositoryRepository.save(userRepository);
                }

                System.out.println("用户[" + username + "]购买了" + quantity + "个盲盒[" + product.getName() +
                                  "]，已创建" + quantity + "条独立记录");
            } else {
                // 普通商品正常合并处理
                UserRepository userRepository = new UserRepository();
                userRepository.setUsername(username);
                userRepository.setProductId(productId);
                userRepository.setProductName(product.getName());
                userRepository.setProductImage(product.getImage());
                userRepository.setQuantity(quantity);
                userRepository.setPrice(product.getPrice());
                userRepository.setOrderId(orderId);
                userRepository.setPurchaseTime(LocalDateTime.now());
                userRepository.setStatus("已付款");
                userRepository.setIsRated(false);

                userRepositoryRepository.save(userRepository);
            }

            // 记录成功购买的商品ID
            successfullyPurchasedProductIds.add(productId);
        }

        // 4. 从购物车中移除已购买的商品
        for (Long productId : successfullyPurchasedProductIds) {
            cartRepository.deleteByUsernameAndProductId(username, productId);
        }

        return Response.buildSuccess("购买成功，订单号: " + orderId);
    }

    @Override
    @Transactional
    public Response<UserRepository> rateProduct(Long repositoryId, Integer rating, String content) {
        // 1. 检查仓库项是否存在
        Optional<UserRepository> repositoryItemOptional = userRepositoryRepository.findById(repositoryId);
        if (!repositoryItemOptional.isPresent()) {
            return Response.buildFailure("找不到指定的购买记录", "404");
        }

        UserRepository item = repositoryItemOptional.get();

        // 2. 检查是否已评价
        if (item.getIsRated()) {
            return Response.buildFailure("此商品已评价", "400");
        }

        // 3. 添加评价
        Comment comment = new Comment();
        comment.setUsername(item.getUsername());
        comment.setProductId(item.getProductId());
        comment.setRating(rating);
        comment.setContent(content);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());

        commentRepository.save(comment);

        // 4. 更新用户仓库项状态
        item.setIsRated(true);
        userRepositoryRepository.save(item);

        return Response.buildSuccess(item);
    }

    @Override
    @Transactional
    public Response<Map<String, Object>> drawMysteryBox(String username, Long itemId) {
        // 1. 检查仓���项是否存在，并且属于该用户
        Optional<UserRepository> repositoryItemOptional = userRepositoryRepository.findById(itemId);
        if (!repositoryItemOptional.isPresent()) {
            return Response.buildFailure("找不到指定的盲盒", "404");
        }

        UserRepository item = repositoryItemOptional.get();
        if (!item.getUsername().equals(username)) {
            return Response.buildFailure("该盲盒不属于您", "403");
        }

        // 检查盲盒是否已经被抽取
        if ("已抽取".equals(item.getStatus())) {
            return Response.buildFailure("该盲盒已被抽取", "400");
        }

        // 2. 获取盲盒对应的商品ID
        Long productId = item.getProductId();
        if (productId == null) {
            return Response.buildFailure("无效的盲盒", "400");
        }

        // 3. 获取盲盒可能包含的物品列表
        List<MysteryBoxItem> boxItems = mysteryBoxItemRepository.findByProductId(productId);

        // 如果盲盒内容未配置，返回错误提示
        if (boxItems.isEmpty()) {
            return Response.buildFailure("该盲盒还未配置抽取内容，请联系管理员", "400");
        }

        // 4. 基于概率随机抽取一个物品
        MysteryBoxItem selectedItem = selectRandomItem(boxItems);
        if (selectedItem == null) {
            return Response.buildFailure("抽取失败，请稍后再试", "500");
        }

        // 5. 如果是库存限制的物品，检查并更新库存
        if (selectedItem.getStock() != null && selectedItem.getStock() > 0) {
            selectedItem.setStock(selectedItem.getStock() - 1);
            mysteryBoxItemRepository.save(selectedItem);
        }

        // 6. 更新盲盒状态为"已抽取"，并保存抽取结果到备注字段
        item.setStatus("已抽取");
        // 使用备注字段保存抽取结果信息
        item.setRemark("抽取结果: " + selectedItem.getName());
        userRepositoryRepository.save(item);

        // 7. 如果抽取结果是实物，添加到用户仓库
        if ("product".equals(selectedItem.getType().toLowerCase())) {
            UserRepository newItem = new UserRepository();
            newItem.setUsername(username);
            newItem.setProductName(selectedItem.getName());
            newItem.setProductImage(selectedItem.getImageUrl());
            newItem.setQuantity(1);
            newItem.setOrderId(generateUniqueOrderId());
            newItem.setPurchaseTime(LocalDateTime.now());
            newItem.setStatus("抽取���得");
            newItem.setIsRated(false);
            newItem.setRemark("从盲盒[" + item.getProductName() + "]抽取获得");

            userRepositoryRepository.save(newItem);
        }

        // 8. 构建抽取结果
        Map<String, Object> result = new HashMap<>();
        // 确保类型与前端期望的一致（小写）
        result.put("type", selectedItem.getType().toLowerCase());
        result.put("name", selectedItem.getName());
        result.put("image", selectedItem.getImageUrl());
        result.put("description", selectedItem.getDescription());

        if ("virtual".equals(selectedItem.getType().toLowerCase()) && selectedItem.getValue() != null) {
            result.put("value", selectedItem.getValue());
        }

        System.out.println("成功抽取盲盒：" + selectedItem.getName() + "，类型：" + selectedItem.getType());

        return Response.buildSuccess(result);
    }

    // 基于概率随机抽取一个物品
    private MysteryBoxItem selectRandomItem(List<MysteryBoxItem> boxItems) {
        // 验证总概率是否接近100%
        BigDecimal totalProbability = BigDecimal.ZERO;
        for (MysteryBoxItem item : boxItems) {
            if (item.getProbability() != null) {
                totalProbability = totalProbability.add(item.getProbability());
            }
        }

        // 概率总和应该接近100
        if (totalProbability.compareTo(new BigDecimal("95")) < 0 ||
            totalProbability.compareTo(new BigDecimal("105")) > 0) {
            // 记录日志，但继续执行
            System.out.println("警告：盲盒物品概率总和不为100%，当前为：" + totalProbability);
        }

        // 随机生成0-100之间的数
        double randomValue = Math.random() * 100;
        double cumulativeProbability = 0.0;

        // 根据累积概率选择物品
        for (MysteryBoxItem item : boxItems) {
            // 跳过库存为0的物品
            if (item.getStock() != null && item.getStock() <= 0) {
                continue;
            }

            BigDecimal itemProbability = item.getProbability() != null ?
                item.getProbability() : BigDecimal.ZERO;

            cumulativeProbability += itemProbability.doubleValue();

            if (randomValue <= cumulativeProbability) {
                return item;
            }
        }

        // 如果因为概率计算问题没有选中，则返回最后一个有效物品
        for (int i = boxItems.size() - 1; i >= 0; i--) {
            MysteryBoxItem item = boxItems.get(i);
            if (item.getStock() == null || item.getStock() > 0) {
                return item;
            }
        }

        return null; // 所有物品库存都为0时
    }

    // 生成唯一订单ID的辅助方法
    private Long generateUniqueOrderId() {
        return System.currentTimeMillis();
    }
}

