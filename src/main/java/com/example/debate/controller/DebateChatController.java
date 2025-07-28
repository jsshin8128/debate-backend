package com.example.debate.controller;

import com.example.debate.dto.DebateChatMessage;
import com.example.debate.dto.DebateChatResponse;
import com.example.debate.dto.DebateJoinMessage;
import com.example.debate.service.DebateRoomService;
import com.example.debate.websocket.WebSocketEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Slf4j
@Controller
public class DebateChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final DebateRoomService debateRoomService;
    private final WebSocketEventListener webSocketEventListener;

    public DebateChatController(SimpMessagingTemplate messagingTemplate, 
                              DebateRoomService debateRoomService,
                              WebSocketEventListener webSocketEventListener) {
        this.messagingTemplate = messagingTemplate;
        this.debateRoomService = debateRoomService;
        this.webSocketEventListener = webSocketEventListener;
    }

    @MessageMapping("/debate/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload DebateChatMessage message) {

        // 정상 메시지는 기존대로 broadcast
        DebateChatResponse response = new DebateChatResponse(
                message.getSender(),
                message.getSenderId(),  // ✅ senderId 포함
                message.getMessage(),
                LocalDateTime.now()
        );
        messagingTemplate.convertAndSend("/topic/debate/" + roomId, response);
    }

    @MessageMapping("/debate/{roomId}/typing")
    public void handleTyping(@DestinationVariable Long roomId,
                             @Payload DebateChatMessage message) {
        messagingTemplate.convertAndSend(
            "/topic/debate/" + roomId + "/typing",  // ✅ 프론트가 구독하는 경로와 일치시킴
            message
        );
    }

    @MessageMapping("/debate/{roomId}/join")
    public void handleJoin(@DestinationVariable Long roomId,
                           @Payload DebateJoinMessage joinMessage,
                           SimpMessageHeaderAccessor headerAccessor) {
        String userId = joinMessage.getSenderId();
        
        if (userId == null) {
            log.warn("❗ senderId is null. Join ignored for roomId: {}", roomId);
            return;
        }
        
        String sessionId = headerAccessor.getSessionId();
        
        // 중복 방지 기능이 포함된 참여자 추적
        boolean success = debateRoomService.trackParticipantWithDuplicateCheck(roomId, userId, sessionId);
        
        if (success) {
            // 세션에 userId 저장 (disconnect 시 제거용)
            webSocketEventListener.storeUserSession(sessionId, userId, roomId);
            log.info("✅ JOIN 성공: roomId={}, userId={}, sessionId={}", roomId, userId, sessionId);
        } else {
            log.warn("❗ JOIN 실패 (중복 접속): roomId={}, userId={}, sessionId={}", roomId, userId, sessionId);
        }
    }
}
