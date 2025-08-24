package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 사용자 엔티티 (불변 객체, 핵심 필드만 노출)
 */
@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;
}
