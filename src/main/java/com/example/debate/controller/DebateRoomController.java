package com.example.debate.controller;

import com.example.debate.dto.*;
import com.example.debate.entity.DebateRoom;
import com.example.debate.service.DebateRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 토론방 REST 컨트롤러
 * - 생성, 조회, 입장/퇴장, 종료, 삭제 등 토론방 관리 엔드포인트 제공
 */
@RestController
@RequestMapping("/debate/rooms")
@RequiredArgsConstructor
public class DebateRoomController {
    private final DebateRoomService debateRoomService;

    /** 토론방 생성 */
    @PostMapping
    public CreateDebateRoomResponse createRoom(@RequestBody CreateDebateRoomRequest request) {
        DebateRoom room = debateRoomService.createRoom(request.getTitle(), request.getDescription());
        return new CreateDebateRoomResponse(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getCreatedAt()
        );
    }

    /** 전체 토론방 목록 조회 */
    @GetMapping
    public List<GetDebateRoomResponse> getAllRooms() {
        return debateRoomService.getAllRooms().stream()
                .map(room -> new GetDebateRoomResponse(
                        room.getId(),
                        room.getTitle(),
                        room.getDescription(),
                        room.getCreatedAt()
                ))
                .toList();
    }

    /** 단일 토론방 조회 */
    @GetMapping("/{roomId}")
    public GetDebateRoomResponse getRoomById(@PathVariable Long roomId) {
        DebateRoom room = debateRoomService.getRoomById(roomId);
        return new GetDebateRoomResponse(
                room.getId(),
                room.getTitle(),
                room.getDescription(),
                room.getCreatedAt()
        );
    }

    /** 토론방 종료 */
    @PatchMapping("/{roomId}")
    public EndDebateRoomResponse endRoom(@PathVariable Long roomId) {
        DebateRoom room = debateRoomService.endRoom(roomId);
        return new EndDebateRoomResponse(room.getId(), room.isClosed());
    }

    /** 토론방 입장 */
    @PostMapping("/{roomId}/join")
    public JoinDebateRoomResponse joinRoom(@PathVariable Long roomId) {
        return debateRoomService.joinRoom(roomId);
    }

    /** 토론방 퇴장 */
    @PatchMapping("/{roomId}/leave")
    public LeaveDebateRoomResponse leaveRoom(@PathVariable Long roomId) {
        return debateRoomService.leaveRoom(roomId);
    }

    /** 토론방 삭제 */
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId) {
        debateRoomService.deleteRoom(roomId);
    }
}
