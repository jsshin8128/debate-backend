package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CreateDebateRoomResponse {
    private Long roomId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
