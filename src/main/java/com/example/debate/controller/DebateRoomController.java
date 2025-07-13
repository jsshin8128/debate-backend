package com.example.debate.controller;

import com.example.debate.entity.DebateRoom;
import com.example.debate.service.DebateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debate/rooms")
@RequiredArgsConstructor
public class DebateRoomController {

    private final DebateRoomService debateRoomService;

    @PostMapping
    public DebateRoom createRoom(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String description = request.get("description");
        return debateRoomService.createRoom(title, description);
    }

    @GetMapping
    public List<DebateRoom> getAllRooms() {
        return debateRoomService.getAllRooms();
    }
}
