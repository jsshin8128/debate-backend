package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 입장 응답 DTO
 * - roomId: 입장한 방 ID
 * - participantCount: 입장 후 전체 인원 수
 */
@Getter
@AllArgsConstructor
public class JoinDebateRoomResponse {
    private Long roomId;
    private int participantCount;
}
