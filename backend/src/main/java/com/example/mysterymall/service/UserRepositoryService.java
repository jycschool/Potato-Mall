package com.example.mysterymall.service;

import com.example.mysterymall.po.UserRepository;
import com.example.mysterymall.vo.Response;

import java.util.List;
import java.util.Map;

public interface UserRepositoryService {
    // 获取用户的所有购买记录
    Response<List<UserRepository>> getUserPurchases(String username);

    // 从购物车购买商品
    Response<String> purchaseFromCart(String username, String address, String contactPhone);

    // 评价已购买的商品
    Response<UserRepository> rateProduct(Long repositoryId, Integer rating, String content);

    // 抽取盲盒
    Response<Map<String, Object>> drawMysteryBox(String username, Long itemId);
}