package com.example.mysterymall.service;

import com.example.mysterymall.po.Comment;
import com.example.mysterymall.vo.Response;

import java.util.List;

public interface CommentService {
    Response<Comment> addComment(Comment comment);
    Response<Comment> updateComment(Comment comment);
    Response<String> deleteComment(Long id);
    Response<Comment> getCommentById(Long id);
    Response<List<Comment>> getCommentsByProductId(Long productId);
    Response<List<Comment>> getCommentsByUsername(String username);
}