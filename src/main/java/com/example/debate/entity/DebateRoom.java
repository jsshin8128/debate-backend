package com.example.debate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 토론방 엔티티
 * - 생성/종료/입장/퇴장 등 상태 관리
 */
@Entity
@Getter
@NoArgsConstructor
public class DebateRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean closed = false;

    @Column(nullable = false)
    private int participantCount = 0;

    /** 토론방 종료 */
    public void end() {
        this.closed = true;
    }

    /** 토론방 입장 */
    public void join() {
        if (!closed) this.participantCount += 1;
    }

    /** 토론방 퇴장 */
    public void leave() {
        if (participantCount > 0) this.participantCount -= 1;
    }
}
