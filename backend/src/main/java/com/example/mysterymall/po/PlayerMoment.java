package com.example.mysterymall.po;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "player_moments")
public class PlayerMoment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moment_id")
    private Long momentId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "images", columnDefinition = "TEXT")
    private String images;

    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "like_count")
    private Integer likeCount = 0;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
    }
}
