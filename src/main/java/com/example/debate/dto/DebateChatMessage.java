package com.example.debate.dto;

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
    private String senderId;
    private String sender;
    private String message;
    private String content;
    private String timestamp;
    private Boolean typing;
}
