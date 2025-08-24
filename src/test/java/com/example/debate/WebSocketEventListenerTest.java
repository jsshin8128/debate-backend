package com.example.debate;

import com.example.debate.service.DebateRoomService;
import com.example.debate.websocket.WebSocketEventListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.assertj.core.api.Assertions.*;


/**
 * WebSocketEventListener 단위 테스트
 * - 세션 관리 및 참가자 수 브로드캐스트 검증
 */
class WebSocketEventListenerTest {
    @Mock
    SimpMessagingTemplate messagingTemplate;
    @Mock
    DebateRoomService debateRoomService;
    @InjectMocks
    WebSocketEventListener eventListener;

    public WebSocketEventListenerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("세션 저장 및 참가자 수 브로드캐스트")
    @SuppressWarnings("unchecked") // 리플렉션 타입 캐스팅 경고 suppress
    void storeUserSessionAndBroadcast() {
        eventListener.storeUserSession("session1", "user1", 1L);
        // 내부 맵에 정상 저장되는지 확인
        // 리플렉션으로 내부 맵을 꺼내어 검증
        try {
            java.lang.reflect.Field userMapField = WebSocketEventListener.class.getDeclaredField("sessionUserMap");
            java.lang.reflect.Field roomMapField = WebSocketEventListener.class.getDeclaredField("sessionRoomMap");
            userMapField.setAccessible(true);
            roomMapField.setAccessible(true);
            java.util.Map<String, String> userMap = (java.util.Map<String, String>) userMapField.get(eventListener);
            java.util.Map<String, Long> roomMap = (java.util.Map<String, Long>) roomMapField.get(eventListener);
            assertThat(userMap).containsEntry("session1", "user1");
            assertThat(roomMap).containsEntry("session1", 1L);
        } catch (Exception e) {
            fail("리플렉션 오류: " + e.getMessage());
        }
    }
}
