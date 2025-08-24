package com.example.auth;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

/**
 * UserRepository CRUD 테스트
 */
@DataJpaTest
class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("회원 저장 및 조회")
    void saveAndFindUser() {
        User user = User.builder().username("user").password("pass").build();
        User saved = userRepository.save(user);
        assertThat(saved.getId()).isNotNull();
        assertThat(userRepository.findByUsername("user")).isPresent();
    }
}
