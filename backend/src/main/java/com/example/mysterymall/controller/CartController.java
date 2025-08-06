package com.example.mysterymall.controller;

import com.example.mysterymall.po.CartItem;
import com.example.mysterymall.service.CartService;
import com.example.mysterymall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Resource
    private CartService cartService;

    /**
     * 获取当前用户的购物车
     */
    @GetMapping
    public Response<List<CartItem>> getUserCart(@RequestParam String username) {
        return cartService.getUserCart(username);
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping
    public Response<?> addToCart(@RequestParam String username,
                                 @RequestParam Long productId,
                                 @RequestParam Integer quantity) {
        return cartService.addToCart(username, productId, quantity);
    }

    /**
     * 从购物车移除商品
     */
    @DeleteMapping("/{productId}")
    public Response<?> removeFromCart(@RequestParam String username,
                                      @PathVariable Long productId) {
        return cartService.removeFromCart(username, productId);
    }

    /**
     * 更新购物车商品数量
     */
    @PutMapping("/{productId}")
    public Response<?> updateCartItemQuantity(@RequestParam String username,
                                              @PathVariable Long productId,
                                              @RequestParam Integer quantity) {
        return cartService.updateCartItemQuantity(username, productId, quantity);
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public Response<?> clearCart(@RequestParam String username) {
        return cartService.clearCart(username);
    }

    /**
     * 选择/取消选择购物车中的商品
     */
    @PutMapping("/{productId}/select")
    public Response<?> selectCartItem(@RequestParam String username,
                                      @PathVariable Long productId,
                                      @RequestParam Boolean selected) {
        return cartService.selectCartItem(username, productId, selected);
    }

    /**
     * 获取已选择的购物车项目
     */
    @GetMapping("/selected")
    public Response<List<CartItem>> getSelectedCartItems(@RequestParam String username) {
        return cartService.getSelectedCartItems(username);
    }

    @GetMapping("/count")
    public Response<Integer> getCartItemCount(@RequestParam String username) {
        List<CartItem> cartItems = cartService.getUserCart(username).getData();
        int count = cartItems.stream().mapToInt(CartItem::getQuantity).sum();
        return Response.buildSuccess(count);
    }
}