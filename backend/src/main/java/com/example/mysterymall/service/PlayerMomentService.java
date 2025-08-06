package com.example.mysterymall.service;

import com.example.mysterymall.po.PlayerMoment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlayerMomentService {

    // 发布动态
    PlayerMoment createMoment(String username, String content, List<String> images);

    // 获取动态列表（分页）
    Page<PlayerMoment> getMoments(Pageable pageable);

    // 获取用户的动态
    List<PlayerMoment> getUserMoments(String username);

    // 删除动态
    void deleteMoment(Long momentId, String username);

    // 点赞动态
    void likeMoment(Long momentId);
}
