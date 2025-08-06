package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.CommentRepository;
import com.example.mysterymall.dao.ProductRepository;
import com.example.mysterymall.po.Comment;
import com.example.mysterymall.service.CommentService;
import com.example.mysterymall.vo.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentRepository commentRepository;

    @Resource
    private ProductRepository productRepository;

    @Override
    public Response<Comment> addComment(Comment comment) {
        // 验证商品是否存在
        if (!productRepository.existsById(comment.getProductId())) {
            return Response.buildFailure("商品不存在", "404");
        }

        Comment savedComment = commentRepository.save(comment);
        return Response.buildSuccess(savedComment);
    }

    @Override
    public Response<Comment> updateComment(Comment comment) {
        // 验证评论是否存在
        Optional<Comment> existingComment = commentRepository.findById(comment.getId());
        if (!existingComment.isPresent()) {
            return Response.buildFailure("评论不存在", "404");
        }

        // 验证是否是同一用户的评论
        if (!existingComment.get().getUsername().equals(comment.getUsername())) {
            return Response.buildFailure("无权修改他人评论", "403");
        }

        Comment updatedComment = commentRepository.save(comment);
        return Response.buildSuccess(updatedComment);
    }

    @Override
    public Response<String> deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            return Response.buildFailure("评论不存在", "404");
        }

        commentRepository.deleteById(id);
        return Response.buildSuccess("删除成功");
    }

    @Override
    public Response<Comment> getCommentById(Long id) {
        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (!commentOptional.isPresent()) {
            return Response.buildFailure("评论不存在", "404");
        }

        return Response.buildSuccess(commentOptional.get());
    }

    @Override
    public Response<List<Comment>> getCommentsByProductId(Long productId) {
        List<Comment> comments = commentRepository.findByProductId(productId);
        return Response.buildSuccess(comments);
    }

    @Override
    public Response<List<Comment>> getCommentsByUsername(String username) {
        List<Comment> comments = commentRepository.findByUsername(username);
        return Response.buildSuccess(comments);
    }
}