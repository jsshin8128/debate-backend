package com.example.debate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebateJoinMessage {
    private String senderId;  // 참여하는 사용자 ID
    private String senderName; // 참여하는 사용자 이름 (선택)
} 