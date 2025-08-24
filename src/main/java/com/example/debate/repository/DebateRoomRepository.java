package com.example.debate.repository;

import com.example.debate.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 토론방 JPA 레포지토리
 * - DebateRoom 엔티티 기본 CRUD 제공
 */
public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
}
