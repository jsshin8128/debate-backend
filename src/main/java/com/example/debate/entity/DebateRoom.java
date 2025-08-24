
package com.example.debate.entity;

import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "토론방 엔티티")
public class DebateRoom {
    @Schema(description = "토론방 ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "토론방 제목")
    @Column(nullable = false)
    private String title;

    @Schema(description = "토론방 설명")
    @Column(columnDefinition = "TEXT")
    private String description;

    @Schema(description = "생성 시각")
    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Schema(description = "방 종료 여부")
    @Column(nullable = false)
    private boolean closed = false;

    @Schema(description = "참가자 수")
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
