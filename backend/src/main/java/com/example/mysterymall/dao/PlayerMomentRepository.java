package com.example.mysterymall.dao;

import com.example.mysterymall.po.PlayerMoment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerMomentRepository extends JpaRepository<PlayerMoment, Long> {

    // 根据用户名查询动态列表
    List<PlayerMoment> findByUsernameOrderByCreateTimeDesc(String username);

    // 获取所有动态（分页）
    Page<PlayerMoment> findAllByOrderByCreateTimeDesc(Pageable pageable);
}
