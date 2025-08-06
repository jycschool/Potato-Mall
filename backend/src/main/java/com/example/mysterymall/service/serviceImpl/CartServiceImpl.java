package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.CartRepository;
import com.example.mysterymall.dao.ProductRepository;
import com.example.mysterymall.po.CartItem;
import com.example.mysterymall.po.Product;
import com.example.mysterymall.service.CartService;
import com.example.mysterymall.vo.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Resource
    private CartRepository cartRepository;

    @Resource
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Response<?> addToCart(String username, Long productId, Integer quantity) {
        if (quantity <= 0) {
            return Response.buildFailure("商品数量必须大于0", "400");
        }

        // 检查商品是否存在
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            return Response.buildFailure("商品不存在", "404");
        }

        Product product = productOptional.get();

        // 检查库存
        if (product.getStock() < quantity) {
            return Response.buildFailure("商品库存不足", "400");
        }

        // 查找购物车中是否已有此商品
        Optional<CartItem> existingItem = cartRepository.findByUsernameAndProductId(username, productId);

        if (existingItem.isPresent()) {
            // 更新已有商品数量
            CartItem cartItem = existingItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;

            // 再次检查库存
            if (product.getStock() < newQuantity) {
                return Response.buildFailure("商品库存不足", "400");
            }

            cartItem.setQuantity(newQuantity);
            cartItem.setUpdateTime(LocalDateTime.now());
            cartRepository.save(cartItem);
            return Response.buildSuccess("商品数量已更新");
        } else {
            // 添加新商品到购物车
            CartItem cartItem = new CartItem();
            cartItem.setUsername(username);
            cartItem.setProductId(productId);
            cartItem.setQuantity(quantity);
            cartItem.setCreateTime(LocalDateTime.now());
            cartItem.setUpdateTime(LocalDateTime.now());
            cartItem.setSelected(true);
            cartRepository.save(cartItem);
            return Response.buildSuccess("商品已添加到购物车");
        }
    }

    @Override
    @Transactional
    public Response<?> removeFromCart(String username, Long productId) {
        Optional<CartItem> cartItemOptional = cartRepository.findByUsernameAndProductId(username, productId);
        if (!cartItemOptional.isPresent()) {
            return Response.buildFailure("购物车中无此商品", "404");
        }

        cartRepository.deleteByUsernameAndProductId(username, productId);
        return Response.buildSuccess("商品已从购物车移除");
    }

    @Override
    @Transactional
    public Response<?> updateCartItemQuantity(String username, Long productId, Integer quantity) {
        if (quantity <= 0) {
            // 如果数量为0或负数，直接移除商品
            return removeFromCart(username, productId);
        }

        Optional<CartItem> cartItemOptional = cartRepository.findByUsernameAndProductId(username, productId);
        if (!cartItemOptional.isPresent()) {
            return Response.buildFailure("购物车中无此商品", "404");
        }

        // 检查商品是否存在
        Optional<Product> productOptional = productRepository.findById(productId);
        if (!productOptional.isPresent()) {
            return Response.buildFailure("商品不存在", "404");
        }

        Product product = productOptional.get();

        // 检查库存
        if (product.getStock() < quantity) {
            return Response.buildFailure("商品库存不足", "400");
        }

        CartItem cartItem = cartItemOptional.get();
        cartItem.setQuantity(quantity);
        cartItem.setUpdateTime(LocalDateTime.now());
        cartRepository.save(cartItem);

        return Response.buildSuccess("商品数量已更新");
    }

    @Override
    public Response<List<CartItem>> getUserCart(String username) {
        List<CartItem> cartItems = cartRepository.findByUsername(username);

        // 获取每个购物车项对应的商品信息
        for (CartItem item : cartItems) {
            Optional<Product> productOptional = productRepository.findById(item.getProductId());
            productOptional.ifPresent(item::setProduct);
        }

        return Response.buildSuccess(cartItems);
    }

    @Override
    @Transactional
    public Response<?> clearCart(String username) {
        cartRepository.deleteAllByUsername(username);
        return Response.buildSuccess("购物车已清空");
    }

    @Override
    @Transactional
    public Response<?> selectCartItem(String username, Long productId, Boolean selected) {
        Optional<CartItem> cartItemOptional = cartRepository.findByUsernameAndProductId(username, productId);
        if (!cartItemOptional.isPresent()) {
            return Response.buildFailure("购物车中无此商品", "404");
        }

        CartItem cartItem = cartItemOptional.get();
        cartItem.setSelected(selected);
        cartItem.setUpdateTime(LocalDateTime.now());
        cartRepository.save(cartItem);

        return Response.buildSuccess(selected ? "商品已选中" : "商品已取消选中");
    }

    @Override
    public Response<List<CartItem>> getSelectedCartItems(String username) {
        List<CartItem> allCartItems = cartRepository.findByUsername(username);
        List<CartItem> selectedItems = allCartItems.stream()
                .filter(CartItem::getSelected)
                .collect(Collectors.toList());

        // 获取每个购物车项对应的商品信息
        for (CartItem item : selectedItems) {
            Optional<Product> productOptional = productRepository.findById(item.getProductId());
            productOptional.ifPresent(item::setProduct);
        }

        return Response.buildSuccess(selectedItems);
    }
}