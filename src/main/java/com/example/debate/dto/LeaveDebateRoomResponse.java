package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 퇴장 응답 DTO
 * - roomId: 퇴장한 방 ID
 * - participantCount: 퇴장 후 남은 인원 수
 */
@Getter
@AllArgsConstructor
public class LeaveDebateRoomResponse {
    private Long roomId;
    private int participantCount;
}
