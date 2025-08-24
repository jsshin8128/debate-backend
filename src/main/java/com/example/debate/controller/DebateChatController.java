package com.example.debate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

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

/**
 * 토론방 채팅 WebSocket 컨트롤러
 * - 메시지 전송, 타이핑 알림, 참여자 입장 처리
 */
@Slf4j
@Controller
@Tag(name = "Debate Chat", description = "토론방 WebSocket 채팅 API")
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

    /**
     * 채팅 메시지 전송 핸들러 (broadcast)
     */
        @Operation(summary = "채팅 메시지 전송", description = "roomId로 채팅 메시지를 브로드캐스트합니다.")
    @MessageMapping("/debate/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload DebateChatMessage message) {
        DebateChatResponse response = new DebateChatResponse(
                message.getSender(),
                message.getSenderId(),
                message.getMessage(),
                LocalDateTime.now()
        );
        messagingTemplate.convertAndSend("/topic/debate/" + roomId, response);
    }

    /**
     * 타이핑 알림 핸들러
     */
        @Operation(summary = "타이핑 알림", description = "roomId로 타이핑 상태를 알립니다.")
    @MessageMapping("/debate/{roomId}/typing")
    public void handleTyping(@DestinationVariable Long roomId,
                             @Payload DebateChatMessage message) {
        messagingTemplate.convertAndSend(
                "/topic/debate/" + roomId + "/typing",
                message
        );
    }

            @Operation(summary = "참여자 입장", description = "roomId로 참여자가 입장합니다.")
    /**
     * 참여자 입장 핸들러 (중복 방지 및 세션 관리)
     */
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
        boolean success = debateRoomService.trackParticipantWithDuplicateCheck(roomId, userId, sessionId);
        if (success) {
            webSocketEventListener.storeUserSession(sessionId, userId, roomId);
            log.info("✅ JOIN 성공: roomId={}, userId={}, sessionId={}", roomId, userId, sessionId);
        } else {
            log.warn("❗ JOIN 실패 (중복 접속): roomId={}, userId={}, sessionId={}", roomId, userId, sessionId);
        }
    }
}
