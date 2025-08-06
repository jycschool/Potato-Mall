DROP TABLE IF EXISTS users;
CREATE TABLE users (
                       username VARCHAR(50) NOT NULL COMMENT '用户名，不允许为空',
                       password VARCHAR(100) NOT NULL COMMENT '用户密码，仅参与插入操作',
                       name VARCHAR(50) NOT NULL COMMENT '用户姓名，不允许为空',
                       avatar VARCHAR(255) COMMENT '用户头像链接',
                       telephone VARCHAR(11) UNIQUE COMMENT '用户手机号，格式需符合1开头的11位数字',
                       email VARCHAR(100) COMMENT '用户邮箱，格式需符合邮箱规范',
                       location VARCHAR(255) COMMENT '用户所在地',
                       role VARCHAR(50) NOT NULL DEFAULT 'user' COMMENT '用户角色：1-管理员，2-用户，3-商家',
                       PRIMARY KEY (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';


-- 插入第一条用户信息
/*
BCrypt的验证机制会使用哈希字符串中存储的完整信息（包括版本标识
2a、工作因子10、以及内置的盐值）来验证密码。当用户输入"123456"时：
BCrypt会从数据库中存储的哈希值 $2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K 提取出盐值
使用这个相同的盐值和工作因子，对用户输入的"123456"进行哈希计算
将计算结果与存储的哈希值进行比较
只要原始密码是"123456"，且存储的哈希值是使用BCrypt正确生成的，验证过程就会成功匹配，用户就能正常登录。
这正是BCrypt的设计优势之一：即使每次加密生成的哈希值都不同，但验证机制可以保证正确的密码总能通过验证。
*/
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user1', -- 用户名（唯一，不允许为空）
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K', -- 密码,加密后的密码，123456->$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K
                                                                                                  'Zhang San', -- 姓名（不允许为空）
                                                                                                  'https://example.com/avatars/johndoe1.jpg', -- 头像 URL
                                                                                                  '13312345678', -- 手机号（符合 1 开头的 11 位数字格式）
                                                                                                  'john.doe1@example.com', -- 邮箱（符合邮箱格式）
                                                                                                  'New York', -- 所在地
                                                                                                  'user' -- 角色：2-用户
                                                                                              );

-- 插入第二条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user2',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'cz',
                                                                                                  'https://example.com/avatars/janesmith2.jpg',
                                                                                                  '13291770128',
                                                                                                  'jane.smith2@example.com',
                                                                                                  'London',
                                                                                                  'user'
                                                                                              );

-- 插入第三条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user3',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'admin',
                                                                                                  'https://example.com/avatars/peterjones3.jpg',
                                                                                                  '12312345678',
                                                                                                  'peter.jones3@example.com',
                                                                                                  'Paris',
                                                                                                  'admin'
                                                                                              );

-- 插入第四条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user4',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'student',
                                                                                                  'https://example.com/avatars/marybrown4.jpg',
                                                                                                  '13300000001',
                                                                                                  'mary.brown4@example.com',
                                                                                                  'Tokyo',
                                                                                                  'uesr'
                                                                                              );

-- 插入第五条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user5',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'David Wilson 5',
                                                                                                  'https://example.com/avatars/davidwilson5.jpg',
                                                                                                  '13544444445',
                                                                                                  'david.wilson5@example.com',
                                                                                                  'Sydney',
                                                                                                  'user'
                                                                                              );

-- 插入第六条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user6',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'Sarah Miller 6',
                                                                                                  'https://example.com/avatars/sarahmiller6.jpg',
                                                                                                  '13455555556',
                                                                                                  'sarah.miller6@example.com',
                                                                                                  'Berlin',
                                                                                                  'user'
                                                                                              );

-- 插入第七条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user7',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'Kevin Anderson 7',
                                                                                                  'https://example.com/avatars/kevinanderson7.jpg',
                                                                                                  '13366666667',
                                                                                                  'kevin.anderson7@example.com',
                                                                                                  'Rome',
                                                                                                  'merchant'
                                                                                              );

-- 插入第八条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user8',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'Laura Thomas 8',
                                                                                                  'https://example.com/avatars/laurathomas8.jpg',
                                                                                                  '13277777778',
                                                                                                  'laura.thomas8@example.com',
                                                                                                  'Madrid',
                                                                                                  'user'
                                                                                              );

-- 插入第九条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user9',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'Chris Jackson 9',
                                                                                                  'https://example.com/avatars/chrisjackson9.jpg',
                                                                                                  '13188888889',
                                                                                                  'chris.jackson9@example.com',
                                                                                                  'Toronto',
                                                                                                  'user'
                                                                                              );

-- 插入第十条用户信息
INSERT INTO users (username, password, name, avatar, telephone, email, location, role) VALUES (
                                                                                                  'user10',
                                                                                                  '$2a$10$mAvRDmcltj4kJ.uWXkNmYekY7jmoRqBYMvi/DSC0Yy4Hib6fL3a8K',
                                                                                                  'Amanda White 10',
                                                                                                  'https://example.com/avatars/amandawhite10.jpg',
                                                                                                  '13099999990',
                                                                                                  'amanda.white10@example.com',
                                                                                                  'Melbourne',
                                                                                                  'user'
                                                                                              );

DROP TABLE IF EXISTS products;
CREATE TABLE products (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL COMMENT '商品名称',
                          description VARCHAR(500) COMMENT '商品描述',
                          price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
                          stock INT NOT NULL COMMENT '库存数量',
                          image VARCHAR(255) COMMENT '商品图片链接',
                          category VARCHAR(50) COMMENT '商品分类'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品表';

CREATE TABLE comments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          product_id BIGINT NOT NULL COMMENT '商品ID',
                          username VARCHAR(50) NOT NULL COMMENT '评论用户',
                          content VARCHAR(500) NOT NULL COMMENT '评论内容',
                          rating INT NOT NULL COMMENT '评分(1-5)',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                          FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                          FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品评论表';

-- iPhone 15 Pro的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (1, 'user1', '使用一周后感觉很满意，拍照性能特别突出，电池续航也比我想象的要好。', 5),
                                                                 (1, 'user2', '整体还不错，但价格确实有点高，希望能有更多优惠活动。', 4),
                                                                 (1, 'user3', 'A17芯片性能确实强大，玩游戏完全没有卡顿，但手机容易发热。', 4);

-- MacBook Air M2的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (2, 'user4', '轻薄便携，电池续航惊人！作为一名程序员，日常开发完全够用。', 5),
                                                                 (2, 'user5', '风扇声音有点大，但性能确实不错，系统流畅。', 4);

-- 纯棉T恤的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (3, 'user6', '面料很舒服，穿着透气，不过洗过之后有点缩水。', 3),
                                                                 (3, 'user7', '颜色很正，质量也不错，值得购买！', 5),
                                                                 (3, 'user1', '这个价格买到这样的T恤很值，会回购其他颜色。', 4);

-- 牛仔裤的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (4, 'user2', '裤型很好看，穿着舒适，就是有点紧。', 4),
                                                                 (4, 'user3', '物超所值，做工精细，面料也很好。', 5);

-- 有机草莓的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (5, 'user4', '草莓很新鲜，酸甜可口，没有农药残留的担忧。', 5),
                                                                 (5, 'user5', '个头有点小，但是味道确实不错。', 4),
                                                                 (5, 'user6', '包装有点简陋，运输过程中有些草莓被压坏了。', 3);

-- 巧克力曲奇的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (6, 'user7', '香脆可口，巧克力味道浓郁，一不小心就吃完了一盒。', 5),
                                                                 (6, 'user1', '甜度刚好，不会太腻，很适合下午茶。', 5);

-- 北欧风沙发的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (7, 'user2', '做工精细，与图片描述一致，安装也很简单。', 5),
                                                                 (7, 'user3', '沙发很漂亮，但是坐感稍硬，需要时间适应。', 4),
                                                                 (7, 'user4', '物流速度快，服务很好，沙发质量不错。', 5);

-- LED台灯的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (8, 'user5', '三档亮度调节很实用，夜间阅读很舒适。', 5),
                                                                 (8, 'user6', '灯光不刺眼，保护视力，充电也方便。', 5);

-- Java编程思想的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (9, 'user7', '经典书籍，内容详实，对Java学习很有帮助。', 5),
                                                                 (9, 'user1', '作为入门书籍有点难度，但是内容确实很全面。', 4),
                                                                 (9, 'user2', '印刷质量好，纸张厚实，值得珍藏的技术书。', 5);

-- 平凡的世界的评论
INSERT INTO comments (product_id, username, content, rating) VALUES
                                                                 (10, 'user3', '震撼人心的作品，读完有很多思考。', 5),
                                                                 (10, 'user4', '经典之作，描绘了那个年代农村的真实生活。', 5);

DROP TABLE IF EXISTS cart_items;
CREATE TABLE cart_items (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            username VARCHAR(50) NOT NULL COMMENT '用户名',
                            product_id BIGINT NOT NULL COMMENT '商品ID',
                            quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
                            selected BOOLEAN NOT NULL DEFAULT true COMMENT '是否选中',
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE,
                            FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
                            UNIQUE KEY uk_user_product (username, product_id) COMMENT '同一用户同一商品只能有一条记录'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='购物车表';

CREATE TABLE user_repository (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '仓库记录ID',
                                 username VARCHAR(50) NOT NULL COMMENT '用户名',
                                 product_id BIGINT NOT NULL COMMENT '商品ID',
                                 product_name VARCHAR(100) NOT NULL COMMENT '商品名称',
                                 product_image VARCHAR(255) DEFAULT NULL COMMENT '商品图片',
                                 quantity INT NOT NULL DEFAULT 1 COMMENT '购买数量',
                                 price DECIMAL(10, 2) NOT NULL COMMENT '购买时的价格',
                                 order_id BIGINT NOT NULL COMMENT '关联订单ID',  -- 关联订单
                                 purchase_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
                                 status VARCHAR(20) NOT NULL DEFAULT '已付款' COMMENT '商品状态',
                                 is_rated TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已评价'
)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户仓库表';

CREATE TABLE mystery_box_items (
                                   id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   product_id BIGINT NOT NULL,
                                   name VARCHAR(100) NOT NULL,
                                   type VARCHAR(20) NOT NULL,
                                   image_url VARCHAR(255),
                                   description VARCHAR(500),
                                   value DECIMAL(10,2),
                                   probability DECIMAL(5,2) NOT NULL,
                                   stock INTEGER,
                                   INDEX (product_id)
);
-- 为ID为1的盲盒添加可能的物品
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, probability, stock)
VALUES (1, '精美手办', 'PHYSICAL', '/images/toy1.jpg', '限量版精美手办', 20.00, 5);

INSERT INTO mystery_box_items (product_id, name, type, image_url, description, probability, stock)
VALUES (1, '潮流玩偶', 'PHYSICAL', '/images/toy2.jpg', '可爱的潮流玩偶', 30.00, 20);

INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability)
VALUES (1, '商城优惠券', 'VIRTUAL', '/images/coupon.jpg', '下次购物可使用的优惠券', 50.00, 50.00);

-- 玩家秀表（朋友圈）
DROP TABLE IF EXISTS player_moments;
CREATE TABLE player_moments (
                                moment_id BIGINT AUTO_INCREMENT COMMENT '玩家秀ID',
                                username VARCHAR(50) NOT NULL COMMENT '发布用户名',
                                content TEXT COMMENT '内容描述',
                                images TEXT COMMENT '图片链接，多个图片以逗号分隔',
                                create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                like_count INT DEFAULT 0 COMMENT '点赞数',
                                PRIMARY KEY (moment_id),
                                FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家秀表';

-- 玩家秀点赞表
DROP TABLE IF EXISTS moment_likes;
CREATE TABLE moment_likes (
                              id BIGINT AUTO_INCREMENT COMMENT '点赞ID',
                              moment_id BIGINT NOT NULL COMMENT '玩家秀ID',
                              username VARCHAR(50) NOT NULL COMMENT '点赞用户名',
                              create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
                              PRIMARY KEY (id),
                              UNIQUE KEY uk_moment_user (moment_id, username) COMMENT '确保用户只能点赞一次',
                              FOREIGN KEY (moment_id) REFERENCES player_moments(moment_id) ON DELETE CASCADE,
                              FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='玩家秀点赞表';

-- 插入示例数据
INSERT INTO player_moments (username, content, images) VALUES
    ('user1', '今天入手了一件超级稀有的物品，非常开心！', 'https://example.com/images/item1.jpg,https://example.com/images/item2.jpg');

-- 示例点赞
INSERT INTO moment_likes (moment_id, username) VALUES (1, 'user2');
