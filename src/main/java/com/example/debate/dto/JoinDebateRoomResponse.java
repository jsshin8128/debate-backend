
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 입장 응답 DTO
 * - roomId: 입장한 방 ID
 * - participantCount: 입장 후 전체 인원 수
 */
@Getter
@AllArgsConstructor
@Schema(description = "토론방 입장 응답 DTO")
public class JoinDebateRoomResponse {
    @Schema(description = "입장한 방 ID")
    private Long roomId;
    @Schema(description = "입장 후 전체 인원 수")
    private int participantCount;
}
