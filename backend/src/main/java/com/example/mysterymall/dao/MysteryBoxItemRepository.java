package com.example.mysterymall.dao;

import com.example.mysterymall.po.MysteryBoxItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MysteryBoxItemRepository extends JpaRepository<MysteryBoxItem, Long> {

    // 根据商品ID(盲盒ID)查询盲盒可能包含的所有物品
    List<MysteryBoxItem> findByProductId(Long productId);
}
