package com.example.mysterymall.dao;

import com.example.mysterymall.po.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> { // String 是主键 username 的类型
    Account findByUsername(String username);
    Account findByTelephone(String account);
}
