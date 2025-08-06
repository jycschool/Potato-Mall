package com.example.mysterymall.service;

import com.example.mysterymall.po.Account;
import com.example.mysterymall.vo.Response;

public interface AccountService {
    public Response<String> createUser(Account account);
    public Response<Account> getUserDetail(String username);
    public Response<String> updateUser(Account account);
    public Response<String> login(String username, String password);
}
