package rent.your.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rent.your.room.dto.RoomDto;
import rent.your.room.service.RoomService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/public")
public class PublicController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms")
    public ResponseEntity<Page<RoomDto>> getPublicRooms(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        Page<RoomDto> rooms;
        if (search != null && !search.isEmpty()) {
            rooms = roomService.searchApprovedRooms(search, pageable);
        } else {
            rooms = roomService.getApprovedRooms(pageable);
        }
        return ResponseEntity.ok(rooms);
    }

    @GetMapping("/rooms/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto room = roomService.getRoomById(id);
        // Optional: Check if the room is approved before returning
        if (!room.isApproved()) {
            // Or throw a specific exception that can be handled by an exception handler
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(room);
    }
}
