package com.example.debate.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebateChatMessage {
    private String type;           // "TYPING", "MESSAGE" 등
    private Long roomId;           // 방 ID
    private String senderId;       // 보낸 사람 ID
    private String sender;         // 보낸 사람 이름 (기존 호환성)
    private String message;        // 메시지 본문
    private String content;        // 입력 중 메시지는 비워도 됨
    private String timestamp;      // 클라이언트에서 전송한 시간 (선택)
    private Boolean typing;        // 실시간 타이핑 표시 여부 (기존 호환성)
}
