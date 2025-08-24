
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토론방 종료 응답 DTO
 * - roomId: 종료된 방 ID
 * - closed: 종료 여부
 */
@Getter
@AllArgsConstructor
@Schema(description = "토론방 종료 응답 DTO")
public class EndDebateRoomResponse {
    @Schema(description = "종료된 방 ID")
    private Long roomId;
    @Schema(description = "방 종료 여부")
    private boolean closed;
}
