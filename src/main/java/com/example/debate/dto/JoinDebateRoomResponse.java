package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JoinDebateRoomResponse {
    private Long roomId;
    private int participantCount;
}
