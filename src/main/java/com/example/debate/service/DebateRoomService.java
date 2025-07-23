package com.example.debate.service;

import com.example.debate.dto.JoinDebateRoomResponse;
import com.example.debate.dto.LeaveDebateRoomResponse;
import com.example.debate.entity.DebateRoom;
import com.example.debate.repository.DebateRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DebateRoomService {

    private final DebateRoomRepository debateRoomRepository;

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
}
