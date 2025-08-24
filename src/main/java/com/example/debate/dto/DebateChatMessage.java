
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 채팅 메시지 DTO (클라이언트 → 서버)
 * - type: 메시지 유형 ("TYPING", "MESSAGE" 등)
 * - roomId: 방 ID
 * - senderId: 보낸 사람 ID
 * - sender: 보낸 사람 이름
 * - message: 메시지 본문
 * - content: 입력 중 메시지는 비워도 됨
 * - timestamp: 클라이언트 전송 시각 (선택)
 * - typing: 실시간 타이핑 표시 여부
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebateChatMessage {
    private String type;
    private Long roomId;
    @Schema(description = "메시지 발신자 이름")
    private String sender;
    @Schema(description = "메시지 발신자 ID")
    private Long senderId;
    @Schema(description = "메시지 본문")
    private String message;
    @Schema(description = "입력 중 메시지는 비워도 됨")
    private String content;
    @Schema(description = "클라이언트 전송 시각 (선택)")
    private String timestamp;
    @Schema(description = "실시간 타이핑 표시 여부")
    private Boolean typing;
}
