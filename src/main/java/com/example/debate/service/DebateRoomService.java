package com.example.debate.service;

import com.example.debate.entity.DebateRoom;
import com.example.debate.repository.DebateRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
