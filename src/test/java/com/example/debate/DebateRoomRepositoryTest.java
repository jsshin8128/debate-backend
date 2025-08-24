package com.example.debate;

import com.example.debate.entity.DebateRoom;
import com.example.debate.repository.DebateRoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.*;

/**
 * DebateRoomRepository CRUD 테스트
 */
@DataJpaTest
class DebateRoomRepositoryTest {
    @Autowired
    DebateRoomRepository debateRoomRepository;

    @Test
    @DisplayName("토론방 저장 및 조회")
    void saveAndFindRoom() {
        DebateRoom room = new DebateRoom();
        // 필드 직접 할당
        java.lang.reflect.Field titleField;
        java.lang.reflect.Field descField;
        try {
            titleField = DebateRoom.class.getDeclaredField("title");
            descField = DebateRoom.class.getDeclaredField("description");
            titleField.setAccessible(true);
            descField.setAccessible(true);
            titleField.set(room, "테스트방");
            descField.set(room, "설명");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DebateRoom saved = debateRoomRepository.save(room);
        assertThat(saved.getId()).isNotNull();
        assertThat(debateRoomRepository.findById(saved.getId())).isPresent();
    }
}
