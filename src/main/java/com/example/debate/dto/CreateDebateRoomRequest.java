package com.example.debate.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 토론방 생성 요청 DTO
 * - title: 방 제목
 * - description: 방 설명
 */
@Getter
@Setter
public class CreateDebateRoomRequest {
    private String title;
    private String description;
}
