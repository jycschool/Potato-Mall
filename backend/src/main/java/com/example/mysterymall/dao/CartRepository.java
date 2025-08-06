package com.example.mysterymall.dao;

import com.example.mysterymall.po.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    // 根据用户名查找所有购物车项
    List<CartItem> findByUsername(String username);

    // 根据用户名和商品ID查找购物车项
    Optional<CartItem> findByUsernameAndProductId(String username, Long productId);

    // 删除指定用户和商品ID的购物车项
    void deleteByUsernameAndProductId(String username, Long productId);

    // 删除指定用户的所有购物车项
    void deleteAllByUsername(String username);
}