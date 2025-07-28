package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

    public User login(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> encoder.matches(rawPassword, user.getPassword()))
                .orElse(null);  // 로그인 실패 시 null 반환
    }
}
