
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 퇴장 응답 DTO
 * - roomId: 퇴장한 방 ID
 * - participantCount: 퇴장 후 남은 인원 수
 */
@Getter
@AllArgsConstructor
@Schema(description = "토론방 퇴장 응답 DTO")
public class LeaveDebateRoomResponse {
    @Schema(description = "퇴장한 방 ID")
    private Long roomId;
    @Schema(description = "퇴장 후 남은 인원 수")
    private int participantCount;
}
