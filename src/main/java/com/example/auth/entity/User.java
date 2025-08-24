
package com.example.auth.entity;

import io.swagger.v3.oas.annotations.media.Schema;

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
@Schema(description = "사용자 엔티티")
public class User {
    @Schema(description = "사용자 ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "사용자명")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Schema(description = "암호화된 비밀번호")
    @Column(nullable = false)
    private String password;
}
