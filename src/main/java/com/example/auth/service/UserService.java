package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 인증/회원가입 서비스
 * - 회원가입 시 중복 체크 및 비밀번호 암호화
 * - 로그인 시 비밀번호 검증
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); // 암호화 인스턴스

    /**
     * 회원가입: 사용자명 중복 체크 및 비밀번호 암호화 후 저장
     */
    public void register(String username, String rawPassword) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자명입니다.");
        }
        String encodedPassword = encoder.encode(rawPassword);
        User user = User.builder()
                .username(username)
                .password(encodedPassword)
                .build();
        userRepository.save(user);
    }

    /**
     * 로그인: 사용자명으로 조회 후 비밀번호 일치 여부 검증
     * @return 성공 시 User, 실패 시 null
     */
    public User login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(rawPassword, user.getPassword()))
                .orElse(null);
    }
}
