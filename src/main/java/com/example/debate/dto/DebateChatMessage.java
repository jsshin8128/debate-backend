package com.example.debate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DebateChatMessage {
    private Long roomId;
    private String sender;
    private String message;
    private String timestamp;
}
