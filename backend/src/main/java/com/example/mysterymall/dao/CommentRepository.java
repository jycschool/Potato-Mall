package com.example.mysterymall.dao;

import com.example.mysterymall.po.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByProductId(Long productId);
    List<Comment> findByUsername(String username);
    void deleteByProductIdAndUsername(Long productId, String username);
}