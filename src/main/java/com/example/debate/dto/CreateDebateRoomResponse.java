
package com.example.debate.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

/**
 * 토론방 생성 응답 DTO
 * - roomId: 생성된 방 ID
 * - title: 방 제목
 * - description: 방 설명
 * - createdAt: 생성 시각
 */
@Getter
@AllArgsConstructor
@Schema(description = "토론방 생성 응답 DTO")
public class CreateDebateRoomResponse {
    @Schema(description = "생성된 방 ID")
    private Long roomId;
    @Schema(description = "방 제목")
    private String title;
    @Schema(description = "방 설명")
    private String description;
    @Schema(description = "생성 시각")
    private LocalDateTime createdAt;
}
