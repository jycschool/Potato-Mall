package com.example.mysterymall.service.serviceImpl;

import com.example.mysterymall.dao.PlayerMomentRepository;
import com.example.mysterymall.po.PlayerMoment;
import com.example.mysterymall.service.PlayerMomentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PlayerMomentServiceImpl implements PlayerMomentService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerMomentServiceImpl.class);

    @Autowired
    private PlayerMomentRepository playerMomentRepository;

    @Override
    @Transactional
    public PlayerMoment createMoment(String username, String content, List<String> images) {
        try {
            logger.info("开始创建玩家秀 - 用户: {}, 内容长度: {}", username, content != null ? content.length() : 0);

            PlayerMoment playerMoment = new PlayerMoment();
            playerMoment.setUsername(username);
            playerMoment.setContent(content);

            // 处理图片，多个图片URL以逗号分隔保存
            if (images != null && !images.isEmpty()) {
                String imagesStr = String.join(",", images);
                playerMoment.setImages(imagesStr);
                logger.info("处理了 {} 张图片", images.size());
            }

            logger.info("即将保存玩家秀到数据库");
            PlayerMoment savedMoment = playerMomentRepository.save(playerMoment);
            logger.info("玩家秀保存成功，ID: {}", savedMoment.getMomentId());

            return savedMoment;
        } catch (Exception e) {
            logger.error("保存玩家秀到数据库时发生错误", e);
            throw new RuntimeException("保存玩家秀失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Page<PlayerMoment> getMoments(Pageable pageable) {
        return playerMomentRepository.findAllByOrderByCreateTimeDesc(pageable);
    }

    @Override
    public List<PlayerMoment> getUserMoments(String username) {
        return playerMomentRepository.findByUsernameOrderByCreateTimeDesc(username);
    }

    @Override
    @Transactional
    public void deleteMoment(Long momentId, String username) {
        PlayerMoment moment = playerMomentRepository.findById(momentId)
                .orElseThrow(() -> new RuntimeException("找不到该动态"));

        // 验证是否是动态发布者
        if (!moment.getUsername().equals(username)) {
            throw new RuntimeException("无权删除该动态");
        }

        playerMomentRepository.deleteById(momentId);
    }

    @Override
    @Transactional
    public void likeMoment(Long momentId) {
        PlayerMoment moment = playerMomentRepository.findById(momentId)
                .orElseThrow(() -> new RuntimeException("找不到该动态"));

        // 增加点赞数
        moment.setLikeCount(moment.getLikeCount() + 1);
        playerMomentRepository.save(moment);
    }
}
