package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 토론방 생성 응답 DTO
 * - roomId: 생성된 방 ID
 * - title: 방 제목
 * - description: 방 설명
 * - createdAt: 생성 시각
 */
@Getter
@AllArgsConstructor
public class CreateDebateRoomResponse {
    private Long roomId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
