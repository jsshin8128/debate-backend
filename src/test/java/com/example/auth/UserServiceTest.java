package com.example.auth;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserService 단위 테스트
 * - 회원가입/로그인 성공 및 예외 상황 검증
 */
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserService userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입 성공")
    void registerSuccess() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        doAnswer(inv -> null).when(userRepository).save(any(User.class));
        userService.register("user", "pass");
    }

    @Test
    @DisplayName("회원가입 중복 예외")
    void registerDuplicate() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new User()));
        assertThatThrownBy(() -> userService.register("user", "pass"))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        User user = User.builder().id(1L).username("user").password(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode("pass")).build();
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(user));
        User result = userService.login("user", "pass");
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("로그인 실패")
    void loginFail() {
        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());
        User result = userService.login("user", "pass");
        assertThat(result).isNull();
    }
}
