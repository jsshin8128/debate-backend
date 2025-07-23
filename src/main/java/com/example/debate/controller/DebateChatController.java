package com.example.debate.controller;

import com.example.debate.dto.DebateChatMessage;
import com.example.debate.dto.DebateChatResponse;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class DebateChatController {

    private final SimpMessagingTemplate messagingTemplate;

    public DebateChatController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/debate/{roomId}/send")
    public void sendMessage(@DestinationVariable Long roomId,
                            @Payload DebateChatMessage message) {

        System.out.println("[채팅 수신] roomId=" + roomId + ", sender=" + message.getSender() + ", message=" + message.getMessage());

        DebateChatResponse response = new DebateChatResponse(
                message.getSender(),
                message.getMessage(),
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSend("/topic/debate/" + roomId, response);
    }
}
