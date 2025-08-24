package com.example.debate.websocket;

import com.example.debate.service.DebateRoomService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 연결/해제 이벤트 리스너
 * - 참가자 세션 관리 및 인원 브로드캐스트
 */
@Component
public class WebSocketEventListener {
    private final SimpMessagingTemplate messagingTemplate;
    private final DebateRoomService debateRoomService;
    // 세션 ID → roomId 매핑
    private final Map<String, Long> sessionRoomMap = new ConcurrentHashMap<>();
    // 세션 ID → userId 매핑
    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    // roomId → 참가자 수 매핑
    private final Map<Long, Integer> roomParticipantCount = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate, DebateRoomService debateRoomService) {
        this.messagingTemplate = messagingTemplate;
        this.debateRoomService = debateRoomService;
    }

    /**
     * 구독 이벤트 핸들러 (참가자 세션 등록 및 인원 브로드캐스트)
     */
    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        String sessionId = sha.getSessionId();
        System.out.println("[SUBSCRIBE] sessionId=" + sessionId + ", destination=" + destination);
        if (destination != null && destination.matches("^/topic/debate/\\d+/participants$")) {
            Long roomId = extractRoomId(destination);
            if (roomId > 0 && sessionId != null) {
                sessionRoomMap.put(sessionId, roomId);
                roomParticipantCount.merge(roomId, 1, Integer::sum);
                System.out.println("[JOIN] roomId=" + roomId + " | sessionId=" + sessionId + " | count=" + roomParticipantCount.get(roomId));
                broadcastCount(roomId);
            }
        }
    }

    /**
     * 연결 해제 이벤트 핸들러 (세션 및 참여자 정리)
     */
    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        Long roomId = sessionRoomMap.remove(sessionId);
        String userId = sessionUserMap.remove(sessionId);
        System.out.println("[DISCONNECT] sessionId=" + sessionId + ", left roomId=" + roomId + ", userId=" + userId);
        if (roomId != null && userId != null) {
            debateRoomService.removeSession(sessionId);
            debateRoomService.removeParticipant(roomId, userId);
            System.out.println("[LEAVE] roomId=" + roomId + " | sessionId=" + sessionId + " | userId=" + userId);
        }
    }

    /**
     * 세션에 userId 저장 (JOIN 메시지 처리 시 호출)
     */
    public void storeUserSession(String sessionId, String userId, Long roomId) {
        sessionUserMap.put(sessionId, userId);
        sessionRoomMap.put(sessionId, roomId);
        System.out.println("[STORE_SESSION] sessionId=" + sessionId + " | userId=" + userId + " | roomId=" + roomId);
    }

    /**
     * destination에서 roomId 추출
     */
    private Long extractRoomId(String destination) {
        try {
            String[] parts = destination.split("/");
            return Long.parseLong(parts[3]); // /topic/debate/{roomId}/participants
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to extract roomId from destination: " + destination);
            return -1L;
        }
    }

    /**
     * 참가자 수 브로드캐스트
     */
    private void broadcastCount(Long roomId) {
        int count = roomParticipantCount.getOrDefault(roomId, 0);
        System.out.println("[BROADCAST] roomId=" + roomId + " | broadcasting count=" + count);
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
    }
}
