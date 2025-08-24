

package com.example.auth.controller;

import com.example.auth.entity.User;
import com.example.auth.service.UserService;
import com.example.auth.dto.SignupRequestDto;
import com.example.auth.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.validation.BindingResult;
import java.util.Map;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated // 입력값 검증 활성화

@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final UserService userService;

    // 응답 메시지 상수화
    private static final String SIGNUP_SUCCESS = "✅ 회원가입 성공";
    private static final String SIGNUP_DUPLICATE = "이미 존재하는 사용자명입니다.";
    private static final String LOGIN_FAIL = "❌ 비밀번호가 틀렸습니다.";

    /**
     * 회원가입: DTO 적용, 입력값 검증 및 중복 사용자 처리
     */
    @Operation(summary = "회원가입", description = "회원가입 요청 처리")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Validated SignupRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 모든 검증 에러 메시지를 리스트로 반환 (null 안전)
            String errorMsg = bindingResult.getAllErrors().isEmpty() ? "입력값 오류" : bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMsg);
        }
        try {
            userService.register(request.getUsername(), request.getPassword());
            return ResponseEntity.ok(SIGNUP_SUCCESS);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(SIGNUP_DUPLICATE);
        }
    }

    /**
     * 로그인: DTO 적용, 입력값 검증 및 실패 응답 일관화
     */
    @Operation(summary = "로그인", description = "로그인 요청 처리")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody @Validated LoginRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getAllErrors().isEmpty() ? "입력값 오류" : bindingResult.getAllErrors().get(0).getDefaultMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", errorMsg));
        }
        User user = userService.login(request.getUsername(), request.getPassword());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", LOGIN_FAIL));
        }
        // 성공 시 최소 정보만 반환
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername()
        ));
    }
}
