package com.example.mysterymall.controller;

import com.example.mysterymall.po.PlayerMoment;
import com.example.mysterymall.service.PlayerMomentService;
import com.example.mysterymall.util.JwtUtil;
import com.example.mysterymall.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/moments")
public class PlayerMomentController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerMomentController.class);

    @Autowired
    private PlayerMomentService playerMomentService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 获取当前用户名
    private String getCurrentUsername(HttpServletRequest request) {
        // 先尝试从 Authorization 头获取 token
        String token = request.getHeader("Authorization");
        logger.debug("Authorization头: {}", token);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtil.extractUsername(token);
            logger.debug("从Authorization头提取的用户名: {}", username);
            return username;
        }

        // 如果Authorization头中没有，再尝试从token头获取
        token = request.getHeader("token");
        logger.debug("Token头: {}", token);

        if (token != null && !token.isEmpty()) {
            String username = jwtUtil.extractUsername(token);
            logger.debug("从token头提取的用户名: {}", username);
            return username;
        }

        logger.warn("未找到有效的认证token");
        return null;
    }

    /**
     * 测试数据库连接并直接输出调试信息
     */
    @GetMapping("/debug")
    public Response<Map<String, Object>> debugDatabase() {
        Map<String, Object> debugInfo = new HashMap<>();
        try {
            // 1. 检查player_moments表是否存在
            logger.info("正在检查player_moments表是否存在...");
            try {
                Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM player_moments", Integer.class);
                debugInfo.put("tableExists", true);
                debugInfo.put("recordCount", count);
                logger.info("player_moments表存在，当前记录数: {}", count);

                // 获取表结构信息
                List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                    "SELECT COLUMN_NAME, DATA_TYPE, IS_NULLABLE, COLUMN_KEY " +
                    "FROM INFORMATION_SCHEMA.COLUMNS " +
                    "WHERE TABLE_NAME = 'player_moments' AND TABLE_SCHEMA = DATABASE()"
                );
                debugInfo.put("tableStructure", columns);
                logger.info("表结构: {}", columns);

                // 检查外键约束
                List<Map<String, Object>> foreignKeys = jdbcTemplate.queryForList(
                    "SELECT CONSTRAINT_NAME, COLUMN_NAME, REFERENCED_TABLE_NAME, REFERENCED_COLUMN_NAME " +
                    "FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
                    "WHERE TABLE_NAME = 'player_moments' AND REFERENCED_TABLE_NAME IS NOT NULL " +
                    "AND TABLE_SCHEMA = DATABASE()"
                );
                debugInfo.put("foreignKeys", foreignKeys);
                logger.info("外键约束: {}", foreignKeys);

            } catch (Exception e) {
                debugInfo.put("tableExists", false);
                debugInfo.put("error", e.getMessage());
                logger.error("检查player_moments表失败: {}", e.getMessage(), e);
            }

            // 2. 测试直接插入数据
            try {
                logger.info("正在尝试直接插入测试数据...");
                jdbcTemplate.update(
                    "INSERT INTO player_moments (username, content, create_time) VALUES (?, ?, NOW())",
                    "test_user", "这是一条调试测试玩家秀"
                );
                debugInfo.put("insertTest", "成功");
                logger.info("测试插入成功");
            } catch (Exception e) {
                debugInfo.put("insertTest", "失败");
                debugInfo.put("insertError", e.getMessage());
                logger.error("测试插入失败: {}", e.getMessage(), e);
            }

            // 3. 检查用户表
            try {
                Integer userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
                debugInfo.put("usersTableExists", true);
                debugInfo.put("userCount", userCount);
                logger.info("users表存在，用户数: {}", userCount);
            } catch (Exception e) {
                debugInfo.put("usersTableExists", false);
                debugInfo.put("usersError", e.getMessage());
                logger.error("检查users表失败: {}", e.getMessage());
            }

            return Response.buildSuccess(debugInfo);
        } catch (Exception e) {
            logger.error("调试过程中发生错误", e);
            return Response.buildFailure("调试失败: " + e.getMessage(), "500");
        }
    }

    /**
     * 发布动态
     */
    @PostMapping
    public Response<PlayerMoment> createMoment(@RequestParam String content,
                                          @RequestParam(required = false) String images,
                                          HttpServletRequest httpRequest) {
        // 输出所有请求头，方便调试
        logger.info("----- 请求头信息 -----");
        java.util.Enumeration<String> headerNames = httpRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("{}: {}", headerName, httpRequest.getHeader(headerName));
        }

        String username = getCurrentUsername(httpRequest);
        logger.info("创建玩家秀请求 - 用户名: {}, 内容: {}, 图片: {}", username,
                    content != null ? content.substring(0, Math.min(20, content.length())) + "..." : "空",
                    images != null ? "有图片" : "无图片");

        if (username == null) {
            logger.warn("未登录用户尝试创建玩家秀");
            return Response.buildFailure("未登录", "401");
        }

        // 检查用户是否存在
        try {
            Integer exists = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?",
                Integer.class,
                username
            );
            if (exists == null || exists == 0) {
                logger.warn("用户 {} 在users表中不存在", username);
                return Response.buildFailure("用户不存在", "404");
            }
            logger.info("确认用户 {} 在users表中存在", username);
        } catch (Exception e) {
            logger.error("检查用户是否存在时出错", e);
        }

        // 处理图片，将逗号分隔的图片字符串转换为列表
        List<String> imageList = null;
        if (images != null && !images.isEmpty()) {
            imageList = Arrays.asList(images.split(","));
            logger.info("处理图片: {} 张图片", imageList.size());
        }

        try {
            logger.info("调用服务层createMoment方法");
            PlayerMoment moment = playerMomentService.createMoment(username, content, imageList);
            logger.info("服务层返回的玩家秀对象: {}", moment);

            if (moment != null && moment.getMomentId() != null) {
                logger.info("玩家秀创建成功: momentId={}", moment.getMomentId());

                // 验证数据确实被保存到了数据库
                try {
                    PlayerMoment saved = jdbcTemplate.queryForObject(
                        "SELECT * FROM player_moments WHERE moment_id = ?",
                        (rs, rowNum) -> {
                            PlayerMoment m = new PlayerMoment();
                            m.setMomentId(rs.getLong("moment_id"));
                            m.setUsername(rs.getString("username"));
                            m.setContent(rs.getString("content"));
                            return m;
                        },
                        moment.getMomentId()
                    );
                    logger.info("通过SQL验证：数据已成功保存到数据库, ID={}", saved.getMomentId());
                } catch (Exception e) {
                    logger.error("验证数据保存失败，无法从数据库查询到刚插入的数据", e);
                }

                return Response.buildSuccess(moment);
            } else {
                logger.error("服务层返回的moment对象为null或momentId为null");
                return Response.buildFailure("创建失败: 服务层返回的数据不完整", "500");
            }
        } catch (Exception e) {
            logger.error("创建玩家秀失败", e);
            return Response.buildFailure("创建失败: " + e.getMessage(), "500");
        }
    }

    /**
     * 获取所有动态（分页）
     */
    @GetMapping
    public Response<Map<String, Object>> getMoments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PlayerMoment> moments = playerMomentService.getMoments(pageable);

        Map<String, Object> result = new HashMap<>();
        result.put("content", moments.getContent());
        result.put("totalPages", moments.getTotalPages());
        result.put("totalElements", moments.getTotalElements());
        result.put("currentPage", moments.getNumber());

        return Response.buildSuccess(result);
    }

    /**
     * 获取用户动态
     */
    @GetMapping("/user/{username}")
    public Response<List<PlayerMoment>> getUserMoments(@PathVariable String username) {
        List<PlayerMoment> moments = playerMomentService.getUserMoments(username);
        return Response.buildSuccess(moments);
    }

    /**
     * 删除动态
     */
    @DeleteMapping("/{momentId}")
    public Response<Void> deleteMoment(@PathVariable Long momentId, HttpServletRequest httpRequest) {
        String username = getCurrentUsername(httpRequest);
        if (username == null) {
            return Response.buildFailure("未登录", "401");
        }

        playerMomentService.deleteMoment(momentId, username);
        return Response.buildSuccess(null);
    }

    /**
     * 点赞动态
     */
    @PostMapping("/{momentId}/like")
    public Response<Void> likeMoment(@PathVariable Long momentId) {
        playerMomentService.likeMoment(momentId);
        return Response.buildSuccess(null);
    }
}
