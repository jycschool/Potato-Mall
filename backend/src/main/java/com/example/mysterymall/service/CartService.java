package com.example.mysterymall.service;

import com.example.mysterymall.po.CartItem;
import com.example.mysterymall.vo.Response;

import java.util.List;

public interface CartService {
    // 添加商品到购物车
    Response<?> addToCart(String username, Long productId, Integer quantity);

    // 从购物车移除商品
    Response<?> removeFromCart(String username, Long productId);

    // 更新购物车中商品数量
    Response<?> updateCartItemQuantity(String username, Long productId, Integer quantity);

    // 获取用户购物车内容
    Response<List<CartItem>> getUserCart(String username);

    // 清空购物车
    Response<?> clearCart(String username);

    // 选择/取消选择购物车中的商品
    Response<?> selectCartItem(String username, Long productId, Boolean selected);

    // 获取已选择的购物车项目
    Response<List<CartItem>> getSelectedCartItems(String username);
}