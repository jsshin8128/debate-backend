package com.example.debate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EndDebateRoomResponse {
    private Long roomId;
    private boolean closed;
}
