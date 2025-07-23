package com.example.debate.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDebateRoomRequest {
    private String title;
    private String description;
}
