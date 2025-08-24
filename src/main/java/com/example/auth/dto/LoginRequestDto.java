
package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 로그인 요청 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {
    @Schema(description = "사용자명")
    @NotBlank(message = "사용자명은 필수입니다.")
    private String username;

    @Schema(description = "비밀번호")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;
}
