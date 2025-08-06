package com.example.mysterymall.dao;

import com.example.mysterymall.po.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepositoryRepository extends JpaRepository<UserRepository, Long> {
    List<UserRepository> findByUsername(String username);
    List<UserRepository> findByUsernameAndProductId(String username, Long productId);
    List<UserRepository> findByOrderId(Long orderId);
}