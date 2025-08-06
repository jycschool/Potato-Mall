package com.example.mysterymall.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_repository")
public class UserRepository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private String productName;

    private String productImage;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    private LocalDateTime purchaseTime = LocalDateTime.now();

    @Column(nullable = false)
    private String status = "已付款";

    @Column(nullable = false)
    private Boolean isRated = false;

    // 添加备注字段，用于存储抽取结果等额外信息
    @Column(length = 500)
    private String remark;
}