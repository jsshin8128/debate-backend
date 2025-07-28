package com.example.auth.controller;

import com.example.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public String signup(
            @RequestParam String username,
            @RequestParam String password
    ) {
        userService.register(username, password);
        return "✅ 회원가입 성공";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String password
    ) {
        boolean success = userService.login(username, password);
        return success ? "✅ 로그인 성공" : "❌ 비밀번호가 틀렸습니다.";
    }
}
