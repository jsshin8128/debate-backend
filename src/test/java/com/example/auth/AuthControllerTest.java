package com.example.auth;

import com.example.auth.controller.AuthController;
import com.example.auth.service.UserService;
import com.example.auth.dto.SignupRequestDto;
import com.example.auth.dto.LoginRequestDto;
import com.example.auth.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * AuthController 단위 테스트
 * - 회원가입/로그인 성공 및 예외 상황 검증
 */
class AuthControllerTest {
    @Mock
    UserService userService;
    @InjectMocks
    AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 성공")
    void signupSuccess() {
        SignupRequestDto req = new SignupRequestDto("user", "pass");
        doNothing().when(userService).register("user", "pass");
        ResponseEntity<String> res = authController.signup(req, null);
        assertThat(res.getBody()).contains("회원가입 성공");
    }

    @Test
    @DisplayName("회원가입 중복 예외")
    void signupDuplicate() {
        SignupRequestDto req = new SignupRequestDto("user", "pass");
        doThrow(new RuntimeException()).when(userService).register(any(), any());
        ResponseEntity<String> res = authController.signup(req, null);
    assertThat(res.getStatusCode().value()).isEqualTo(400);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        LoginRequestDto req = new LoginRequestDto("user", "pass");
        User user = User.builder().id(1L).username("user").password("pass").build();
        when(userService.login("user", "pass")).thenReturn(user);
        ResponseEntity<?> res = authController.login(req, null);
    assertThat(res.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFail() {
        LoginRequestDto req = new LoginRequestDto("user", "wrong");
        when(userService.login(any(), any())).thenReturn(null);
        ResponseEntity<?> res = authController.login(req, null);
    assertThat(res.getStatusCode().value()).isEqualTo(401);
    }
}
