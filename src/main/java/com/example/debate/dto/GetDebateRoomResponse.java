package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetDebateRoomResponse {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
