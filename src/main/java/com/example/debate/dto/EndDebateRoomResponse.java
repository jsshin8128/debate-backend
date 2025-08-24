package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 종료 응답 DTO
 * - roomId: 종료된 방 ID
 * - closed: 종료 여부
 */
@Getter
@AllArgsConstructor
public class EndDebateRoomResponse {
    private Long roomId;
    private boolean closed;
}
