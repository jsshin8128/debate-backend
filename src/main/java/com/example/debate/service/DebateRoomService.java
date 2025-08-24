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

/**
 * 토론방 서비스
 * - 생성, 조회, 입장/퇴장, 종료, 삭제 등 토론방 관리 및 참여자 추적
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DebateRoomService {
    private final DebateRoomRepository debateRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final Map<Long, Set<String>> participantMap = new ConcurrentHashMap<>();
    // 중복 방지용: sessionId → userId 매핑
    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    // 중복 방지용: userId → sessionId 매핑
    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    /** 토론방 생성 */
    public DebateRoom createRoom(String title, String description) {
        DebateRoom room = new DebateRoom();
        // 생성자만 있으므로, 생성 후 필드 직접 할당 (리플렉션 대신 생성자 확장 권장)
        setField(room, "title", title);
        setField(room, "description", description);
        return debateRoomRepository.save(room);
    }

    // DebateRoom 필드 할당용 private 메서드
    private void setField(DebateRoom room, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = DebateRoom.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(room, value);
        } catch (Exception e) {
            throw new RuntimeException("토론방 필드 할당 오류: " + fieldName, e);
        }
    }

    /** 전체 토론방 목록 조회 */
    public List<DebateRoom> getAllRooms() {
        return debateRoomRepository.findAll();
    }

    /** 단일 토론방 조회 */
    public DebateRoom getRoomById(Long id) {
        return debateRoomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 토론방이 존재하지 않습니다. ID=" + id));
    }

    /** 토론방 종료 */
    @Transactional
    public DebateRoom endRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        room.end();
        return room;
    }

    /** 토론방 입장 */
    @Transactional
    public JoinDebateRoomResponse joinRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        if (room.isClosed()) {
            throw new IllegalStateException("이미 종료된 토론방입니다.");
        }
        room.join();
        DebateRoom updated = debateRoomRepository.save(room);
        return new JoinDebateRoomResponse(updated.getId(), updated.getParticipantCount());
    }

    /** 토론방 퇴장 */
    @Transactional
    public LeaveDebateRoomResponse leaveRoom(Long roomId) {
        DebateRoom room = getRoomById(roomId);
        room.leave();
        DebateRoom updated = debateRoomRepository.save(room);
        return new LeaveDebateRoomResponse(updated.getId(), updated.getParticipantCount());
    }

    /** 토론방 삭제 */
    @Transactional
    public void deleteRoom(Long roomId) {
        if (!debateRoomRepository.existsById(roomId)) {
            throw new IllegalArgumentException("존재하지 않는 방입니다: " + roomId);
        }
        debateRoomRepository.deleteById(roomId);
    }

    /** 참여자 추적 및 브로드캐스트 */
    public void trackParticipant(Long roomId, String userId) {
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        int count = participantMap.get(roomId).size();
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
    }

    /** 참여자 제거 및 브로드캐스트 */
    public void removeParticipant(Long roomId, String userId) {
        Set<String> participants = participantMap.get(roomId);
        if (participants != null) {
            participants.remove(userId);
            int count = participants.size();
            messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
        }
    }

    /** 중복 방지 기능이 포함된 참여자 추적 */
    public boolean trackParticipantWithDuplicateCheck(Long roomId, String userId, String sessionId) {
        String existingSessionId = userSessionMap.get(userId);
        if (existingSessionId != null && !existingSessionId.equals(sessionId)) {
            log.warn("❗ 중복 접속 감지: userId={}, existingSessionId={}, newSessionId={}",
                    userId, existingSessionId, sessionId);
            return false;
        }
        sessionUserMap.put(sessionId, userId);
        userSessionMap.put(userId, sessionId);
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
        int count = participantMap.get(roomId).size();
        messagingTemplate.convertAndSend("/topic/debate/" + roomId + "/participants", count);
        log.info("✅ 참여자 추가: roomId={}, userId={}, sessionId={}, count={}",
                roomId, userId, sessionId, count);
        return true;
    }

    /** 세션 제거 (disconnect 시) */
    public void removeSession(String sessionId) {
        String userId = sessionUserMap.remove(sessionId);
        if (userId != null) {
            userSessionMap.remove(userId);
            log.info("✅ 세션 제거: sessionId={}, userId={}", sessionId, userId);
        }
    }

    /** 참여자 직접 추가 */
    public void addUser(Long roomId, String userId) {
        participantMap.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(userId);
    }

    /** 방별 고유 참여자 수 반환 */
    public int getUniqueUserCount(Long roomId) {
        return participantMap.getOrDefault(roomId, Collections.emptySet()).size();
    }
}
