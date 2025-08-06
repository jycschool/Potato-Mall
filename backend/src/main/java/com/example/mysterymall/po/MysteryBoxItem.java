package com.example.mysterymall.po;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 盲盒内容表，定义每个商品(盲盒)可能包含的物品及其概率
 */
@Entity
@Table(name = "mystery_box_items")
public class MysteryBoxItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 关联的商品ID(盲盒ID)
    @Column(nullable = false)
    private Long productId;

    // 奖品名称
    @Column(nullable = false, length = 100)
    private String name;

    // 奖品类型: PHYSICAL(实物奖品), VIRTUAL(虚拟奖品)
    @Column(nullable = false, length = 20)
    private String type;

    // 奖品图片URL
    @Column(length = 255)
    private String imageUrl;

    // 奖品描述
    @Column(length = 500)
    private String description;

    // 虚拟奖品的价值(如果是虚拟奖品)
    @Column(precision = 10, scale = 2)
    private BigDecimal value;

    // 中奖概率(0-100的数字)
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal probability;

    // 库存数量(针对稀有实物奖品)
    @Column
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
