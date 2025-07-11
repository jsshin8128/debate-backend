package com.example.debate.repository;

import com.example.debate.entity.DebateRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DebateRoomRepository extends JpaRepository<DebateRoom, Long> {
}
