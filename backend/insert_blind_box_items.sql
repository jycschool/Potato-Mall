-- 盲盒抽奖物品数据（与产品表严格一一对应）
-- 注意：product_id需要根据实际数据库中的ID进行调整

-- 清空现有数据（如果需要）
-- TRUNCATE TABLE mystery_box_items;

-- 1. 夏日限定盲盒（10款不同夏日沙滩玩偶）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(1, '夏日沙滩女孩', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01P9v8r21TduwBBhbCL_!!2200707537348.jpg', '夏日限定系列款，手持冰淇淋的沙滩女孩', 59.90, 15.00, 75),
(1, '冲浪男孩', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01C3Um3P1TduwCYIKN8_!!2200707537348.jpg', '夏日限定系列款，带冲浪板的男孩', 59.90, 15.00, 75),
(1, '沙滩椰子树', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01ZRzGYy1TduwDBbJXy_!!2200707537348.jpg', '夏日限定系列款，沙滩椰子树场景', 59.90, 10.00, 50),
(1, '沙滩排球企鹅', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01hwmFYO1TduwEzKRFR_!!2200707537348.jpg', '夏日限定系列款，打排球的企鹅', 59.90, 10.00, 50),
(1, '沙滩小螃蟹', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01mPP4ev1TduwF2jsjV_!!2200707537348.jpg', '夏日限定系列款，沙滩上的红色小螃蟹', 59.90, 10.00, 50),
(1, '热带鱼', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01ub6jWQ1TduwGNYI6U_!!2200707537348.jpg', '夏日限定系列款，五彩斑斓的热带鱼', 59.90, 10.00, 50),
(1, '沙滩城堡', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01wmJLNl1TduwHUghB0_!!2200707537348.jpg', '夏日限定系列款，精致的沙滩城堡', 59.90, 10.00, 50),
(1, '沙滩救生员', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01pCxJMK1TduwJbs2Xf_!!2200707537348.jpg', '夏日限定系列款，救生员形象', 59.90, 10.00, 50),
(1, '夏日小狗', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01nyLuuI1TduwKmSm0T_!!2200707537348.jpg', '夏日限定系列款，戴墨镜的沙滩小狗', 59.90, 5.00, 25),
(1, '金色海豚（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01wB6hqs1TduwLnkWjZ_!!2200707537348.jpg', '夏日限定系列稀有款，镀金海豚造型', 159.90, 5.00, 25);

-- 2. 星空奇缘盲盒（12款神秘星座守护精灵）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(2, '白羊座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01sY1EIu1TduwNPHvpb_!!2200707537348.jpg', '星空奇缘系列，白羊座守护精灵', 49.90, 8.00, 48),
(2, '金牛座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01fGOtGH1TduwPvY0La_!!2200707537348.jpg', '星空奇缘系列，金牛座守护精灵', 49.90, 8.00, 48),
(2, '双子座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01L6LBmD1TduwQxDsDX_!!2200707537348.jpg', '星空奇缘系列，双子座守护精灵', 49.90, 8.00, 48),
(2, '巨蟹座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01FJjhfa1TduwSYjRUc_!!2200707537348.jpg', '星空奇缘系列，巨蟹座守护精灵', 49.90, 8.00, 48),
(2, '狮子座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01jRiccY1TduwTExLKY_!!2200707537348.jpg', '星空奇缘系列，狮子座守护精灵', 49.90, 8.00, 48),
(2, '处女座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01tm7OF41TduwVfFIMi_!!2200707537348.jpg', '星空奇缘系列，处女座守护精灵', 49.90, 8.00, 48),
(2, '天秤座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN0138KqeM1TduwWLMkjE_!!2200707537348.jpg', '星空奇缘系列，天秤座守护精灵', 49.90, 8.00, 48),
(2, '天蝎座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01t0L2a31TduwXMcRir_!!2200707537348.jpg', '星空奇缘系列，天蝎座守护精灵', 49.90, 8.00, 48),
(2, '射手座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01bBxqgW1TduwYyqeTM_!!2200707537348.jpg', '星空奇缘系列，射手座守护精灵', 49.90, 8.00, 48),
(2, '摩羯座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01lrvT0v1TduwaSyUZO_!!2200707537348.jpg', '星空奇缘系列，摩羯座守护精灵', 49.90, 8.00, 48),
(2, '水瓶座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01H1CMRY1TduwcElMtT_!!2200707537348.jpg', '星空奇缘系列，水瓶座守护精灵', 49.90, 8.00, 48),
(2, '双鱼座守护精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01QFxjmg1TduwZVJnvb_!!2200707537348.jpg', '星空奇缘系列，双鱼座守护精灵', 49.90, 12.00, 72);

-- 3. 探险家联盟盲盒（8款不同冒险家角色）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(3, '热带丛林探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01V11dQK1Tduwb1gYlS_!!2200707537348.jpg', '探险家联盟系列，热带丛林探险家，配备指南针和防蛇装备', 69.90, 13.00, 52),
(3, '沙漠探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01qGIcfg1Tduwch5hRy_!!2200707537348.jpg', '探险家联盟系列，沙漠探险家，配备水壶和遮阳帽', 69.90, 13.00, 52),
(3, '极地探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN014NX4qv1Tduwe9p7Wb_!!2200707537348.jpg', '探险家联盟系列，极地探险家，配备雪橇和保暖装备', 69.90, 13.00, 52),
(3, '深海探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01TP2D9z1Tduwfo4XJ3_!!2200707537348.jpg', '探险家联盟系列，深海探险家，配备潜水装备', 69.90, 13.00, 52),
(3, '高山探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01IDFN0W1TduwhJlXYs_!!2200707537348.jpg', '探险家联盟系列，高山探险家，配备登山装备', 69.90, 13.00, 52),
(3, '洞穴探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01bK5y3o1TduwiaNarv_!!2200707537348.jpg', '探险家联盟系列，洞穴探险家，配备头灯和绳索', 69.90, 13.00, 52),
(3, '古迹探险家', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01bu5QSW1Tduwkn1hys_!!2200707537348.jpg', '探险家联盟系列，古迹探险家，配备古籍和探测器', 69.90, 13.00, 52),
(3, '太空探险家（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01hKGlVf1TduwmUQpwc_!!2200707537348.jpg', '探险家联盟系列稀有款，太空探险家，配备宇航服和氧气罐', 169.90, 9.00, 36);

-- 4. 萌宠乐园盲盒（12款不同萌宠形象）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(4, '柴犬', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01ktxfWL1TduwoAhAhm_!!2200707537348.jpg', '萌宠乐园系列，可爱柴犬，戴红色围巾', 45.90, 8.50, 59),
(4, '布偶猫', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01Ysusep1TduwpXFoFb_!!2200707537348.jpg', '萌宠乐园系列，蓝眼布偶猫', 45.90, 8.50, 59),
(4, '垂耳兔', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01A5I4PY1Tduwqvt2xN_!!2200707537348.jpg', '萌宠乐园系列，长耳垂耳兔', 45.90, 8.50, 59),
(4, '小仓鼠', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01M61zni1Tduwsaqx4a_!!2200707537348.jpg', '萌宠乐园系列，圆滚滚小仓鼠', 45.90, 8.50, 59),
(4, '哈士奇', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01hs3PSk1TduwtZCLhQ_!!2200707537348.jpg', '萌宠乐园系列，调皮哈士奇', 45.90, 8.50, 59),
(4, '金毛犬', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01VZ1KrL1TduwvFUuGJ_!!2200707537348.jpg', '萌宠乐园系列，忠诚金毛犬', 45.90, 8.50, 59),
(4, '英短猫', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01inlnJw1TduwwOSLoN_!!2200707537348.jpg', '萌宠乐园系列，英国短毛猫', 45.90, 8.50, 59),
(4, '小鸭子', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01XbUhRB1Tduwy6Bb5d_!!2200707537348.jpg', '萌宠乐园系列，黄色小鸭子', 45.90, 8.50, 59),
(4, '小猪', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01sQTyir1TduwzeaAAj_!!2200707537348.jpg', '萌宠乐园系列，粉色小猪', 45.90, 8.50, 59),
(4, '小松鼠', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01Y9o2Hi1Tduyey36OL_!!2200707537348.jpg', '萌宠乐园系列，活泼小松鼠', 45.90, 8.50, 59),
(4, '柯基犬', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01Y9o2Hi1Tdux1vHCIl_!!2200707537348.jpg', '萌宠乐园系列，短腿柯基犬', 45.90, 8.50, 59),
(4, '彩虹独角兽（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01bHkmHt1TduygfBXOw_!!2200707537348.jpg', '萌宠乐园系列稀有款，彩虹独角兽', 145.90, 6.50, 61);

-- 5. 深海探秘盲盒（10款神秘深海生物）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(5, '蓝鲸', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01IAs4QK1TduyiLIDUq_!!2200707537348.jpg', '深海探秘系列，发光蓝鲸', 79.90, 10.50, 31),
(5, '章鱼', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01u9eCYJ1Tduyk1Pxkl_!!2200707537348.jpg', '深海探秘系列，多触手章鱼', 79.90, 10.50, 31),
(5, '海马', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01ooOgsw1TduylhXEga_!!2200707537348.jpg', '深海探秘系列，荧光海马', 79.90, 10.50, 31),
(5, '水母', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01QqRXI71TduynNdzQF_!!2200707537348.jpg', '深海探秘系列，透明发光水母', 79.90, 10.50, 31),
(5, '鲨鱼', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01eWNolL1TduyozfXgY_!!2200707537348.jpg', '深海探秘系列，深海鲨鱼', 79.90, 10.50, 31),
(5, '海龟', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01gBMCdj1TduyqbkH2N_!!2200707537348.jpg', '深海探秘系列，长寿海龟', 79.90, 10.50, 31),
(5, '海豹', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01NEbH3W1TduysHp5Lx_!!2200707537348.jpg', '深海探秘系列，萌萌海豹', 79.90, 10.50, 31),
(5, '电鳗', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01nEcpHB1Tduytzwl1B_!!2200707537348.jpg', '深海探秘系列，电光闪烁电鳗', 79.90, 10.50, 31),
(5, '珊瑚礁鱼群', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN016xDDe41TduyvGepv7_!!2200707537348.jpg', '深海探秘系列，彩色珊瑚礁鱼群', 79.90, 10.50, 31),
(5, '深海巨妖（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01TkaSow1TduywwehVH_!!2200707537348.jpg', '深海探秘系列稀有款，传说中的深海巨妖', 179.90, 5.50, 21);

-- 6. 圣诞奇缘盲盒（8款圣诞老人和小精灵）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(6, '圣诞老人', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01Hua1Rg1Tdux1vHCIl_!!2200707537348.jpg', '圣诞奇缘系列，传统圣诞老人', 69.90, 13.00, 52),
(6, '红鼻子驯鹿', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01jGWWOW1Tdux3YjA3J_!!2200707537348.jpg', '圣诞奇缘系列，鲁道夫红鼻子驯鹿', 69.90, 13.00, 52),
(6, '圣诞雪人', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN017XJBAr1Tdux5EBFN9_!!2200707537348.jpg', '圣诞奇缘系列，快乐圣诞雪人', 69.90, 13.00, 52),
(6, '圣诞帽精灵', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01hzAOjT1Tdux6rVZza_!!2200707537348.jpg', '圣诞奇缘系列，绿色圣诞精灵', 69.90, 13.00, 52),
(6, '圣诞树', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01tC4y8f1Tdux8YiSVw_!!2200707537348.jpg', '圣诞奇缘系列，装饰圣诞树', 69.90, 13.00, 52),
(6, '圣诞袜', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01tP6hC31TduxAENJGc_!!2200707537348.jpg', '圣诞奇缘系列，红色圣诞袜', 69.90, 13.00, 52),
(6, '姜饼人', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01oGNbRo1TduxCzMpR4_!!2200707537348.jpg', '圣诞奇缘系列，可爱姜饼人', 69.90, 13.00, 52),
(6, '金色圣诞铃铛（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01VK5woX1TduxEbyGNb_!!2200707537348.jpg', '圣诞奇缘系列稀有款，镀金圣诞铃铛', 169.90, 9.00, 36);

-- 7. 新年祝福盲盒（12款中国十二生肖形象）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(7, '鼠年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01z6diwz1TduxGHFprO_!!2200707537348.jpg', '新年祝福系列，生肖鼠，灵活机智', 59.90, 8.00, 40),
(7, '牛年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01fvI4PO1TduxHxNPRm_!!2200707537348.jpg', '新年祝福系列，生肖牛，勤劳坚韧', 59.90, 8.00, 40),
(7, '虎年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01r2bMqv1TduxJcx08R_!!2200707537348.jpg', '新年祝福系列，生肖虎，威武勇敢', 59.90, 8.00, 40),
(7, '兔年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01tDj1qK1TduxLJCc9x_!!2200707537348.jpg', '新年祝福系列，生肖兔，温柔敏捷', 59.90, 8.00, 40),
(7, '龙年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01gCPAUq1TduxN2FRzP_!!2200707537348.jpg', '新年祝福系列，生肖龙，祥瑞华贵', 59.90, 8.00, 40),
(7, '蛇年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01YQ60P51TduxOiOPkw_!!2200707537348.jpg', '新年祝福系列，生肖蛇，机智灵动', 59.90, 8.00, 40),
(7, '马年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01MwT8G91TduxQOVMlS_!!2200707537348.jpg', '新年祝福系列，生肖马，奔腾自由', 59.90, 8.00, 40),
(7, '羊年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01OIgr6Y1TduxS4dtOj_!!2200707537348.jpg', '新年祝福系列，生肖羊，温顺祥和', 59.90, 8.00, 40),
(7, '猴年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01gSXaZt1TduxTkm2Ge_!!2200707537348.jpg', '新年祝福系列，生肖猴，聪明活泼', 59.90, 8.00, 40),
(7, '鸡年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01lkXsWO1TduxVQtNmH_!!2200707537348.jpg', '新年祝福系列，生肖鸡，报晓守时', 59.90, 8.00, 40),
(7, '狗年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01rsnNrP1TduxXAdhmJ_!!2200707537348.jpg', '新年祝福系列，生肖狗，忠诚守护', 59.90, 8.00, 40),
(7, '猪年生肖', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01znLWY71TduxYnd3CX_!!2200707537348.jpg', '新年祝福系列，生肖猪，福气满满', 59.90, 12.00, 60);

-- 8. 职场精英盲盒（10款不同职业人物）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(8, 'CEO', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01YH2sUh1TduxaUjtlp_!!2200707537348.jpg', '职场精英系列，决策者CEO，西装笔挺', 55.90, 10.50, 47),
(8, '金融分析师', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01vWyDug1TduxcAryWw_!!2200707537348.jpg', '职场精英系列，金融分析师，手持图表', 55.90, 10.50, 47),
(8, '程序员', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01k33JuO1Tduxdri0Sc_!!2200707537348.jpg', '职场精英系列，技术程序员，带电脑', 55.90, 10.50, 47),
(8, '设计师', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01ZBNh3T1TduxgJBHfN_!!2200707537348.jpg', '职场精英系列，创意设计师，彩色画板', 55.90, 10.50, 47),
(8, '市场营销', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01nty8Gr1Tduxhzua9O_!!2200707537348.jpg', '职场精英系列，市场营销，拿着扬声器', 55.90, 10.50, 47),
(8, '人力资源', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01mAVkU41TduxjgzMu5_!!2200707537348.jpg', '职场精英系列，人力资源，握着简历', 55.90, 10.50, 47),
(8, '律师', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01XJ90Q31TduxlN6BRW_!!2200707537348.jpg', '职场精英系列，专业律师，拿着法典', 55.90, 10.50, 47),
(8, '销售精英', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01hzn2IN1TduxmXWRhx_!!2200707537348.jpg', '职场精英系列，销售精英，握手姿势', 55.90, 10.50, 47),
(8, '企业顾问', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN019ESXWM1TduxoDdgcn_!!2200707537348.jpg', '职场精英系列，企业顾问，带公文包', 55.90, 10.50, 47),
(8, '金色商业领袖（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01CerIAN1TduxptylWO_!!2200707537348.jpg', '职场精英系列稀有款，镀金商业领袖', 155.90, 5.50, 25);

-- 9. 医护天使盲盒（8款不同科室的医护人员）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(9, '外科医生', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01U8KtY91TduxrZ6YLn_!!2200707537348.jpg', '医护天使系列，外科医生，手术服装', 65.90, 13.00, 45),
(9, '内科医生', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01v2WjCL1TduxtFDr0y_!!2200707537348.jpg', '医护天使系列，内科医生，听诊器', 65.90, 13.00, 45),
(9, '儿科医生', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01MopO3a1TduxuaWDCN_!!2200707537348.jpg', '医护天使系列，儿科医生，抱着小熊', 65.90, 13.00, 45),
(9, '急诊医生', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01AUIkIS1TduxwGd8P3_!!2200707537348.jpg', '医护天使系列，急诊医生，急救包', 65.90, 13.00, 45),
(9, '护士长', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01MSwVfW1Tduxxa9kVt_!!2200707537348.jpg', '医护天使系列，护士长，拿着病历本', 65.90, 13.00, 45),
(9, '麻醉师', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01of8pvC1TduxzIhvAf_!!2200707537348.jpg', '医护天使系列，麻醉师，带口罩', 65.90, 13.00, 45),
(9, '放射科医生', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01tG6eaI1Tduy0wP6lt_!!2200707537348.jpg', '医护天使系列，放射科医生，X光片', 65.90, 13.00, 45),
(9, '金色医学院士（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01ZA1ZfZ1Tduy2cW9nj_!!2200707537348.jpg', '医护天使系列稀有款，镀金医学院士', 165.90, 9.00, 35);

-- 10. 童话王国盲盒（10款经典童话人物）
INSERT INTO mystery_box_items (product_id, name, type, image_url, description, value, probability, stock) VALUES
(10, '灰姑娘', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01WRYDTp1Tduy4IbY5u_!!2200707537348.jpg', '童话王国系列，水晶鞋灰姑娘', 49.90, 10.00, 55),
(10, '小红帽', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01MfNGGO1Tduy5odTRo_!!2200707537348.jpg', '童话王国系列，带篮子的小红帽', 49.90, 10.00, 55),
(10, '白雪公主', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01JrVMGO1Tduy7UM7fW_!!2200707537348.jpg', '童话王国系列，咬苹果的白雪公主', 49.90, 10.00, 55),
(10, '睡美人', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01bNqlUt1Tduy91adSN_!!2200707537348.jpg', '童话王国系列，沉睡的公主', 49.90, 10.00, 55),
(10, '长发公主', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01dYy2Xx1TduyAhfXg1_!!2200707537348.jpg', '童话王国系列，金色长发公主', 49.90, 10.00, 55),
(10, '美人鱼', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN013FrJ711TduyCNm3bo_!!2200707537348.jpg', '童话王国系列，美丽人鱼公主', 49.90, 10.00, 55),
(10, '青蛙王子', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i1/2200707537348/O1CN01Cab1Zn1TduyE3QEjH_!!2200707537348.jpg', '童话王国系列，带皇冠的青蛙', 49.90, 10.00, 55),
(10, '三只小猪', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i2/2200707537348/O1CN01GUHFU51TduyFjXBJD_!!2200707537348.jpg', '童话王国系列，三只小猪套装', 49.90, 10.00, 55),
(10, '木偶奇遇记', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i3/2200707537348/O1CN01QdKZmM1TduyHPepmL_!!2200707537348.jpg', '童话王国系列，小木偶匹诺曹', 49.90, 10.00, 55),
(10, '金色童话城堡（稀有款）', 'PHYSICAL', 'https://img.alicdn.com/imgextra/i4/2200707537348/O1CN01z7f2sA1TduyJ5mPLR_!!2200707537348.jpg', '童话王国系列稀有款，镀金童话城堡', 149.90, 10.00, 55);

-- 后面的盲盒数据同样以此类推，确保每个盲盒的抽奖物品与其描述严格对应
