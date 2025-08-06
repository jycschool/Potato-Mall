package com.example.mysterymall.controller;

import com.example.mysterymall.po.UserRepository;
import com.example.mysterymall.service.UserRepositoryService;
import com.example.mysterymall.vo.RateRequest;
import com.example.mysterymall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user-repository")
public class UserRepositoryController {

    @Resource
    private UserRepositoryService userRepositoryService;

    @GetMapping
    public Response<List<UserRepository>> getUserPurchases(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return userRepositoryService.getUserPurchases(username);
    }

    @PostMapping("/purchase")
    public Response<String> purchaseFromCart(
            @RequestParam String address,
            @RequestParam String contactPhone,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return userRepositoryService.purchaseFromCart(username, address, contactPhone);
    }

    @PostMapping("/rate/{repositoryId}")
    public Response<UserRepository> rateProduct(
            @PathVariable Long repositoryId,
            @Valid @RequestBody RateRequest rateRequest) {
        return userRepositoryService.rateProduct(repositoryId, rateRequest.getRating(), rateRequest.getContent());
    }

    @PostMapping("/draw-mystery-box/{itemId}")
    public Response<Map<String, Object>> drawMysteryBox(
            @PathVariable Long itemId,
            HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        return userRepositoryService.drawMysteryBox(username, itemId);
    }
}