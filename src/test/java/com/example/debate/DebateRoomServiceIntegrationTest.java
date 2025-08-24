package com.example.debate;

import com.example.debate.entity.DebateRoom;
import com.example.debate.repository.DebateRoomRepository;
import com.example.debate.service.DebateRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * DebateRoomService 통합 테스트
 * - 토론방 생성, 조회, 입장/퇴장, 종료, 삭제 등 핵심 기능 검증
 */
@SpringBootTest
@Transactional
class DebateRoomServiceIntegrationTest {
    @Autowired
    DebateRoomService debateRoomService;
    @Autowired
    DebateRoomRepository debateRoomRepository;

    @Test
    @DisplayName("토론방 생성 및 단일 조회")
    void createAndGetRoom() {
        DebateRoom room = debateRoomService.createRoom("테스트방", "설명");
        DebateRoom found = debateRoomService.getRoomById(room.getId());
        assertThat(found.getTitle()).isEqualTo("테스트방");
        assertThat(found.getDescription()).isEqualTo("설명");
    }

    @Test
    @DisplayName("전체 토론방 목록 조회")
    void getAllRooms() {
        debateRoomService.createRoom("A", "descA");
        debateRoomService.createRoom("B", "descB");
        List<DebateRoom> rooms = debateRoomService.getAllRooms();
        assertThat(rooms.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    @DisplayName("토론방 입장/퇴장")
    void joinAndLeaveRoom() {
        DebateRoom room = debateRoomService.createRoom("입장방", "desc");
        int before = room.getParticipantCount();
        debateRoomService.joinRoom(room.getId());
        DebateRoom afterJoin = debateRoomService.getRoomById(room.getId());
        assertThat(afterJoin.getParticipantCount()).isEqualTo(before + 1);
        debateRoomService.leaveRoom(room.getId());
        DebateRoom afterLeave = debateRoomService.getRoomById(room.getId());
        assertThat(afterLeave.getParticipantCount()).isEqualTo(before);
    }

    @Test
    @DisplayName("토론방 종료")
    void endRoom() {
        DebateRoom room = debateRoomService.createRoom("종료방", "desc");
        debateRoomService.endRoom(room.getId());
        DebateRoom ended = debateRoomService.getRoomById(room.getId());
        assertThat(ended.isClosed()).isTrue();
    }

    @Test
    @DisplayName("토론방 삭제")
    void deleteRoom() {
        DebateRoom room = debateRoomService.createRoom("삭제방", "desc");
        debateRoomService.deleteRoom(room.getId());
        assertThatThrownBy(() -> debateRoomService.getRoomById(room.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
