
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

/**
 * 토론방 생성 요청 DTO
 * - title: 방 제목
 * - description: 방 설명
 */
@Getter
@Setter
@Schema(description = "토론방 생성 요청 DTO")
public class CreateDebateRoomRequest {
    @Schema(description = "토론방 제목")
    private String title;
    @Schema(description = "토론방 설명")
    private String description;
}
