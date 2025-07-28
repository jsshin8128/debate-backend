package com.example.debate.service;

import com.example.debate.dto.JoinDebateRoomResponse;
import com.example.debate.dto.LeaveDebateRoomResponse;
import com.example.debate.entity.DebateRoom;
import com.example.debate.repository.DebateRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebateRoomService {

    private final DebateRoomRepository debateRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, Set<String>> participantMap = new ConcurrentHashMap<>();
    
    // 중복 방지용: sessionId → userId 매핑
    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    
    // 중복 방지용: userId → sessionId 매핑 (한 사용자가 여러 세션으로 접속 방지)
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    public DebateRoom createRoom(String title, String description) {
        DebateRoom room = new DebateRoom();
        room.setTitle(title);
        room.setDescription(description);
        return debateRoomRepository.save(room);
    }

    public List<DebateRoom> getAllRooms() {
        return debateRoomRepository.findAll();
    }

    public DebateRoom getRoomById(Long id) {
        return debateRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 토론방이 존재하지 않습니다. ID=" + id));
    }

    @Transactional
    public DebateRoom endRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        room.end(); 
        return room;
    }

    @Transactional
    public JoinDebateRoomResponse joinRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        if (room.isClosed()) {
            throw new IllegalStateException("이미 종료된 토론방입니다.");
        }
        room.join(); // 참여자 수 증가
        DebateRoom updated = debateRoomRepository.save(room);
        return new JoinDebateRoomResponse(updated.getId(), updated.getParticipantCount());
    }

    @Transactional
    public LeaveDebateRoomResponse leaveRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        room.leave(); // 참여자 수 감소
        DebateRoom updated = debateRoomRepository.save(room);
        return new LeaveDebateRoomResponse(updated.getId(), updated.getParticipantCount());
    }

    @Transactional
    public void deleteRoom(Long roomId) {
        if (!debateRoomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("존재하지 않는 방입니다: " + roomId);
        }
        debateRoomRepository.deleteById(roomId);
    }

    public void trackParticipant(Long roomId, String userId) {
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);

        int count = participantMap.get(roomId).size();
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
    }

    public void removeParticipant(Long roomId, String userId) {
        Set<String> participants = participantMap.get(roomId);
        if (participants != null) {
            participants.remove(userId);
            int count = participants.size();
            messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
        }
    }

    // 중복 방지 기능이 포함된 참여자 추적
    public boolean trackParticipantWithDuplicateCheck(Long roomId, String userId, String sessionId) {
        // 기존 세션 확인
        String existingSessionId = userSessionMap.get(userId);
        if (existingSessionId != null && !existingSessionId.equals(sessionId)) {
            log.warn("❗ 중복 접속 감지: userId={}, existingSessionId={}, newSessionId={}", 
                    userId, existingSessionId, sessionId);
            return false; // 중복 접속으로 처리하지 않음
        }

        // 세션 매핑 저장
        sessionUserMap.put(sessionId, userId);
        userSessionMap.put(userId, sessionId);
        
        // 참여자 추가
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);

        int count = participantMap.get(roomId).size();
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
        
        log.info("✅ 참여자 추가: roomId={}, userId={}, sessionId={}, count={}", 
                roomId, userId, sessionId, count);
        return true;
    }

    // 세션 제거 (disconnect 시)
    public void removeSession(String sessionId) {
        String userId = sessionUserMap.remove(sessionId);
        if (userId != null) {
            userSessionMap.remove(userId);
            log.info("✅ 세션 제거: sessionId={}, userId={}", sessionId, userId);
        }
    }

    public void addUser(Long roomId, String userId) {
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    public int getUniqueUserCount(Long roomId) {
        return participantMap.getOrDefault(roomId, Collections.emptySet()).size();
    }
}
