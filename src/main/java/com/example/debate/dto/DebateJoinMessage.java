
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

/**
 * 토론방 입장 메시지 DTO
 * - senderId: 참여자 ID
 * - senderName: 참여자 이름 (선택)
 */
@Getter
@Setter
@Schema(description = "토론방 입장 메시지 DTO")
public class DebateJoinMessage {
    @Schema(description = "참여하는 사용자 ID")
    private String senderId;  // 참여하는 사용자 ID
    @Schema(description = "참여하는 사용자 이름 (선택)")
    private String senderName; // 참여하는 사용자 이름 (선택)
}