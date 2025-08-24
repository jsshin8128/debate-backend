
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 채팅 메시지 응답 DTO
 * - sender: 보낸 사람 이름
 * - senderId: 보낸 사람 ID
 * - message: 메시지 본문
 * - timestamp: 서버 수신 시각
 */
@Getter
@AllArgsConstructor
@Schema(description = "채팅 응답 DTO")
public class DebateChatResponse {
    @Schema(description = "메시지 발신자 이름")
    private String sender;
    @Schema(description = "메시지 발신자 ID")
    private Long senderId;  // 본인 메시지 판단용
    @Schema(description = "메시지 본문")
    private String message;
    @Schema(description = "메시지 전송 시각")
    private LocalDateTime timestamp;
}
