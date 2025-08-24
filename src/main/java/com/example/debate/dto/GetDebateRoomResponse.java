package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 토론방 단일/목록 조회 응답 DTO
 * - id: 방 ID
 * - title: 방 제목
 * - description: 방 설명
 * - createdAt: 생성 시각
 */
@Getter
@AllArgsConstructor
public class GetDebateRoomResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
