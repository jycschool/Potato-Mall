package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.MysteryBoxItemRepository;
import com.example.mysterymall.po.MysteryBoxItem;
import com.example.mysterymall.service.MysteryBoxItemService;
import com.example.mysterymall.vo.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 盲盒内容服务实现类
 */
@Service
public class MysteryBoxItemServiceImpl implements MysteryBoxItemService {

    @Resource
    private MysteryBoxItemRepository mysteryBoxItemRepository;

    @Override
    public Response<List<MysteryBoxItem>> getItemsByProductId(Long productId) {
        if (productId == null) {
            return Response.buildFailure("产品ID不能为空", "400");
        }

        List<MysteryBoxItem> items = mysteryBoxItemRepository.findByProductId(productId);
        return Response.buildSuccess(items);
    }

    @Override
    @Transactional
    public Response<MysteryBoxItem> addMysteryBoxItem(MysteryBoxItem mysteryBoxItem) {
        if (mysteryBoxItem == null) {
            return Response.buildFailure("盲盒内容不能为空", "400");
        }

        if (mysteryBoxItem.getProductId() == null) {
            return Response.buildFailure("产品ID不能为空", "400");
        }

        if (mysteryBoxItem.getName() == null || mysteryBoxItem.getName().trim().isEmpty()) {
            return Response.buildFailure("奖品名称不能为空", "400");
        }

        if (mysteryBoxItem.getType() == null || mysteryBoxItem.getType().trim().isEmpty()) {
            return Response.buildFailure("奖品类型不能为空", "400");
        }

        if (mysteryBoxItem.getProbability() == null || mysteryBoxItem.getProbability().compareTo(BigDecimal.ZERO) <= 0) {
            return Response.buildFailure("奖品概率必须大于0", "400");
        }

        // 规范化类型名称为小写（product或virtual）
        String type = mysteryBoxItem.getType().toLowerCase();
        if (!type.equals("product") && !type.equals("virtual")) {
            return Response.buildFailure("奖品类型必须是product或virtual", "400");
        }
        mysteryBoxItem.setType(type);

        // 验证该盲盒的总概率是否超过100
        List<MysteryBoxItem> existingItems = mysteryBoxItemRepository.findByProductId(mysteryBoxItem.getProductId());
        BigDecimal totalProbability = BigDecimal.ZERO;
        for (MysteryBoxItem item : existingItems) {
            if (item.getProbability() != null) {
                totalProbability = totalProbability.add(item.getProbability());
            }
        }

        BigDecimal newTotalProbability = totalProbability.add(mysteryBoxItem.getProbability());
        if (newTotalProbability.compareTo(new BigDecimal("100")) > 0) {
            return Response.buildFailure("添加该奖品后总概率将超过100%，当前总概率: " + totalProbability + "%", "400");
        }

        // 保存盲盒内容
        MysteryBoxItem savedItem = mysteryBoxItemRepository.save(mysteryBoxItem);
        return Response.buildSuccess(savedItem);
    }

    @Override
    @Transactional
    public Response<MysteryBoxItem> updateMysteryBoxItem(Long itemId, MysteryBoxItem mysteryBoxItem) {
        if (itemId == null || mysteryBoxItem == null) {
            return Response.buildFailure("参数不能为空", "400");
        }

        Optional<MysteryBoxItem> existingItemOpt = mysteryBoxItemRepository.findById(itemId);
        if (!existingItemOpt.isPresent()) {
            return Response.buildFailure("找不到指定的盲盒内容", "404");
        }

        MysteryBoxItem existingItem = existingItemOpt.get();

        // 如果概率发生变化，需要验证总概率
        if (mysteryBoxItem.getProbability() != null &&
            !mysteryBoxItem.getProbability().equals(existingItem.getProbability())) {

            BigDecimal probabilityDifference = mysteryBoxItem.getProbability().subtract(existingItem.getProbability());

            // 计算新的总概率
            List<MysteryBoxItem> allItems = mysteryBoxItemRepository.findByProductId(existingItem.getProductId());
            BigDecimal totalProbability = BigDecimal.ZERO;
            for (MysteryBoxItem item : allItems) {
                if (item.getId().equals(itemId)) continue; // 跳过当前项
                if (item.getProbability() != null) {
                    totalProbability = totalProbability.add(item.getProbability());
                }
            }

            BigDecimal newTotalProbability = totalProbability.add(mysteryBoxItem.getProbability());
            if (newTotalProbability.compareTo(new BigDecimal("100")) > 0) {
                return Response.buildFailure("更新后总概率将超过100%，当前其他项总概率: " + totalProbability + "%", "400");
            }
        }

        // 更新字段（只更新非空字段��
        if (mysteryBoxItem.getName() != null && !mysteryBoxItem.getName().trim().isEmpty()) {
            existingItem.setName(mysteryBoxItem.getName());
        }

        if (mysteryBoxItem.getType() != null && !mysteryBoxItem.getType().trim().isEmpty()) {
            // 规范化类型名称为小写
            String type = mysteryBoxItem.getType().toLowerCase();
            if (!type.equals("product") && !type.equals("virtual")) {
                return Response.buildFailure("奖品类型必须是product或virtual", "400");
            }
            existingItem.setType(type);
        }

        if (mysteryBoxItem.getImageUrl() != null) {
            existingItem.setImageUrl(mysteryBoxItem.getImageUrl());
        }

        if (mysteryBoxItem.getDescription() != null) {
            existingItem.setDescription(mysteryBoxItem.getDescription());
        }

        if (mysteryBoxItem.getValue() != null) {
            existingItem.setValue(mysteryBoxItem.getValue());
        }

        if (mysteryBoxItem.getProbability() != null) {
            existingItem.setProbability(mysteryBoxItem.getProbability());
        }

        if (mysteryBoxItem.getStock() != null) {
            existingItem.setStock(mysteryBoxItem.getStock());
        }

        MysteryBoxItem updatedItem = mysteryBoxItemRepository.save(existingItem);
        return Response.buildSuccess(updatedItem);
    }

    @Override
    @Transactional
    public Response<String> deleteMysteryBoxItem(Long itemId) {
        if (itemId == null) {
            return Response.buildFailure("物品ID不能为空", "400");
        }

        Optional<MysteryBoxItem> itemOpt = mysteryBoxItemRepository.findById(itemId);
        if (!itemOpt.isPresent()) {
            return Response.buildFailure("找不到指定的盲盒内容", "404");
        }

        mysteryBoxItemRepository.deleteById(itemId);
        return Response.buildSuccess("删除成功");
    }

    @Override
    public Response<Map<String, Object>> drawRandomItem(Long productId) {
        if (productId == null) {
            return Response.buildFailure("产品ID不能为空", "400");
        }

        // 获取盲盒可能的内容列表
        List<MysteryBoxItem> boxItems = mysteryBoxItemRepository.findByProductId(productId);
        if (boxItems.isEmpty()) {
            return Response.buildFailure("该盲盒还未配置抽取内容", "404");
        }

        // 随机抽取物品
        MysteryBoxItem selectedItem = selectRandomItem(boxItems);
        if (selectedItem == null) {
            return Response.buildFailure("抽取失败，可能所有物品库存均为0", "500");
        }

        // 如果抽取的物品有库存限制，更新库存
        if (selectedItem.getStock() != null && selectedItem.getStock() > 0) {
            selectedItem.setStock(selectedItem.getStock() - 1);
            mysteryBoxItemRepository.save(selectedItem);
        }

        // 构建抽取结果
        Map<String, Object> result = new HashMap<>();
        result.put("id", selectedItem.getId());
        result.put("type", selectedItem.getType().toLowerCase());
        result.put("name", selectedItem.getName());
        result.put("image", selectedItem.getImageUrl());
        result.put("description", selectedItem.getDescription());

        if ("virtual".equalsIgnoreCase(selectedItem.getType()) && selectedItem.getValue() != null) {
            result.put("value", selectedItem.getValue());
        }

        System.out.println("成功抽取盲盒物品：" + selectedItem.getName() + "，类型：" + selectedItem.getType());
        return Response.buildSuccess(result);
    }

    @Override
    public Response<Boolean> isItemsConfigured(Long productId) {
        if (productId == null) {
            return Response.buildFailure("产品ID不能为空", "400");
        }

        List<MysteryBoxItem> items = mysteryBoxItemRepository.findByProductId(productId);
        boolean isConfigured = !items.isEmpty();

        return Response.buildSuccess(isConfigured);
    }

    @Override
    public Response<Map<String, Object>> validateProbabilities(Long productId) {
        if (productId == null) {
            return Response.buildFailure("产品ID不能为空", "400");
        }

        List<MysteryBoxItem> items = mysteryBoxItemRepository.findByProductId(productId);
        BigDecimal totalProbability = BigDecimal.ZERO;

        for (MysteryBoxItem item : items) {
            if (item.getProbability() != null) {
                totalProbability = totalProbability.add(item.getProbability());
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalProbability", totalProbability);
        result.put("isValid", totalProbability.compareTo(new BigDecimal("100")) == 0);
        result.put("itemCount", items.size());

        return Response.buildSuccess(result);
    }

    @Override
    @Transactional
    public Response<Integer> updateItemStock(Long itemId, Integer stockChange) {
        if (itemId == null || stockChange == null) {
            return Response.buildFailure("参数不能为空", "400");
        }

        Optional<MysteryBoxItem> itemOpt = mysteryBoxItemRepository.findById(itemId);
        if (!itemOpt.isPresent()) {
            return Response.buildFailure("找不到指定的盲盒内容", "404");
        }

        MysteryBoxItem item = itemOpt.get();
        Integer currentStock = item.getStock();

        // 如果当前库存为null，表示不限制库存
        if (currentStock == null) {
            if (stockChange > 0) {
                item.setStock(stockChange);
            } else {
                return Response.buildFailure("该物品不限制库存", "400");
            }
        } else {
            // 计算新库存
            int newStock = currentStock + stockChange;
            if (newStock < 0) {
                return Response.buildFailure("库存不足，当前库存: " + currentStock, "400");
            }
            item.setStock(newStock);
        }

        mysteryBoxItemRepository.save(item);
        return Response.buildSuccess(item.getStock());
    }

    /**
     * 基于概率随机抽取一个物品
     */
    private MysteryBoxItem selectRandomItem(List<MysteryBoxItem> boxItems) {
        // 计算总概率
        BigDecimal totalProbability = BigDecimal.ZERO;

        System.out.println("==================== 抽取盲盒开始 ====================");
        System.out.println("盲盒物品总数: " + boxItems.size());

        // 首先过滤出可用的物品（有库存的）
        List<MysteryBoxItem> availableItems = new ArrayList<>();

        for (MysteryBoxItem item : boxItems) {
            // 只考虑有库存或不限库存的物品
            if ((item.getStock() == null || item.getStock() > 0) && item.getProbability() != null) {
                availableItems.add(item);
                totalProbability = totalProbability.add(item.getProbability());

                System.out.println("可抽取物品: ID=" + item.getId() + ", 名称=" + item.getName() +
                        ", 类型=" + item.getType() + ", 概率=" + item.getProbability() +
                        ", 库存=" + (item.getStock() == null ? "不限" : item.getStock()));
            } else {
                System.out.println("跳过物品: ID=" + item.getId() + ", 名称=" + item.getName() +
                        ", 原因: " + (item.getProbability() == null ? "概率为空" :
                        (item.getStock() != null && item.getStock() <= 0 ? "库存不足" : "未知")));
            }
        }

        if (availableItems.isEmpty()) {
            System.out.println("没有可抽取的物品，总概率为0");
            return null; // 没有可抽取的物品
        }

        System.out.println("有效物品总概率: " + totalProbability);

        // 如果总概率不为100%，输出警告并进行概率归一化
        if (totalProbability.compareTo(new BigDecimal("95")) < 0 ||
                totalProbability.compareTo(new BigDecimal("105")) > 0) {

            System.out.println("警告：盲盒物品概率总和不为100%，当前为：" + totalProbability + "，进行概率归一化处理");

            // 对概率进行归一化处理
            for (MysteryBoxItem item : availableItems) {
                // 归一化：新概率 = (原概率 / 总概率) * 100
                BigDecimal normalizedProbability = item.getProbability()
                        .divide(totalProbability, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(new BigDecimal("100"));

                System.out.println("物品 [" + item.getName() + "] 概率调整: " +
                        item.getProbability() + " -> " + normalizedProbability);

                // 这里只是在本次抽取中使用归一化概率，不修改数据库中的值
                item.setProbability(normalizedProbability);
            }
        }

        // 使用更可靠的随机数生成
        // 使用当前纳秒作为种子增加随机性
        Random random = new Random(System.nanoTime());
        double randomValue = random.nextDouble() * 100.0; // 0-100范围
        System.out.println("生成的随机值: " + randomValue);

        // 基于累积概率选择物品
        double cumulativeProbability = 0.0;
        MysteryBoxItem selectedItem = null;

        // 为了增加随机性，随机打乱物品列表顺序
        Collections.shuffle(availableItems, random);

        for (MysteryBoxItem item : availableItems) {
            cumulativeProbability += item.getProbability().doubleValue();
            System.out.println("检查物品: ID=" + item.getId() + ", 名称=" + item.getName() +
                    ", 累积概率=" + cumulativeProbability);

            if (randomValue <= cumulativeProbability) {
                selectedItem = item;
                System.out.println("命中物品: " + item.getName() + " (随机值 " + randomValue +
                        " <= 累积概率 " + cumulativeProbability + ")");
                break;
            }
        }

        // 如果没有选中任何物品（可能由于浮点误差），选择最后一个物品
        if (selectedItem == null && !availableItems.isEmpty()) {
            selectedItem = availableItems.get(availableItems.size() - 1);
            System.out.println("未通过累积概率选中任何物品，使用最后物品: " + selectedItem.getName());
        }

        if (selectedItem != null) {
            System.out.println("最终抽取物品: " + selectedItem.getName() + ", 类型: " + selectedItem.getType());
        } else {
            System.out.println("抽取失败，未选中任何物品");
        }

        System.out.println("==================== 抽取盲盒结束 ====================");
        return selectedItem;
    }
}
