package com.example.debate.dto;

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
public class DebateChatResponse {
    private String sender;
    private String senderId;  // 본인 메시지 판단용
    private String message;
    private LocalDateTime timestamp;
}
