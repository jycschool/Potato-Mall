package com.example.mysterymall.controller;

import com.example.mysterymall.po.Comment;
import com.example.mysterymall.service.CommentService;
import com.example.mysterymall.vo.Response;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 添加评论
     */
    @PostMapping
    public Response<Comment> addComment(@Valid @RequestBody Comment comment, HttpServletRequest request) {
        // 从JWT中获取用户名
        String username = (String) request.getAttribute("username");
        comment.setUsername(username);
        return commentService.addComment(comment);
    }

    /**
     * 更新评论
     */
    @PutMapping("/{id}")
    public Response<Comment> updateComment(@PathVariable Long id, @Valid @RequestBody Comment comment, HttpServletRequest request) {
        // 从JWT中获取用户名
        String username = (String) request.getAttribute("username");
        comment.setId(id);
        comment.setUsername(username);
        return commentService.updateComment(comment);
    }

    /**
     * 删除评论
     */
    @DeleteMapping("/{id}")
    public Response<String> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        // 从JWT获取用户名并检查权限（在service层实现）
        return commentService.deleteComment(id);
    }

    /**
     * 获取特定评论
     */
    @GetMapping("/{id}")
    public Response<Comment> getComment(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    /**
     * 获取商品的所有评论
     */
    @GetMapping("/product/{productId}")
    public Response<List<Comment>> getProductComments(@PathVariable Long productId) {
        return commentService.getCommentsByProductId(productId);
    }

    /**
     * 获取用户的所有评论
     */
    @GetMapping("/user/{username}")
    public Response<List<Comment>> getUserComments(@PathVariable String username) {
        return commentService.getCommentsByUsername(username);
    }
}