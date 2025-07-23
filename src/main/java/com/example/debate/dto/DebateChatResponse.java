package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class DebateChatResponse {
    private String sender;
    private String message;
    private LocalDateTime timestamp;
}
