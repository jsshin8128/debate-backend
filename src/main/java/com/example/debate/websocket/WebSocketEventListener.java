package com.example.debate.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final SimpMessagingTemplate messagingTemplate;

    // 세션 ID → roomId 매핑
    private final Map<String, Long> sessionRoomMap = new ConcurrentHashMap<>();

    // roomId → 참가자 수 매핑
    private final Map<Long, Integer> roomParticipantCount = new ConcurrentHashMap<>();

    public WebSocketEventListener(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSubscribe(SessionSubscribeEvent event) {
        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
        String destination = sha.getDestination();
        String sessionId = sha.getSessionId();

        if (destination != null && destination.matches("^/topic/debate/\\d+/participants$")) {
            Long roomId = extractRoomId(destination);
            if (roomId > 0 && sessionId != null) {
                sessionRoomMap.put(sessionId, roomId);
                roomParticipantCount.merge(roomId, 1, Integer::sum);
                broadcastCount(roomId);
            }
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        Long roomId = sessionRoomMap.remove(sessionId);

        if (roomId != null) {
            roomParticipantCount.computeIfPresent(roomId, (k, v) -> Math.max(0, v - 1));
            broadcastCount(roomId);
        }
    }

    private Long extractRoomId(String destination) {
        try {
            String[] parts = destination.split("/");
            return Long.parseLong(parts[3]); // /topic/debate/{roomId}/participants
        } catch (Exception e) {
            return -1L;
        }
    }

    private void broadcastCount(Long roomId) {
        int count = roomParticipantCount.getOrDefault(roomId, 0);
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
    }
}
