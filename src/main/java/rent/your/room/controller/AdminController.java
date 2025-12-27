package rent.your.room.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rent.your.room.dto.RoomDto;
import rent.your.room.service.RoomService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/rooms/pending")
    public ResponseEntity<Page<RoomDto>> getPendingRooms(Pageable pageable) {
        Page<RoomDto> pendingRooms = roomService.getPendingRooms(pageable);
        return ResponseEntity.ok(pendingRooms);
    }

    @PutMapping("/rooms/{id}/approve")
    public ResponseEntity<RoomDto> approveRoom(@PathVariable Long id) {
        RoomDto approvedRoom = roomService.approveRoom(id);
        return ResponseEntity.ok(approvedRoom);
    }

    @PutMapping("/rooms/{id}/reject")
    public ResponseEntity<RoomDto> rejectRoom(@PathVariable Long id) {
        RoomDto rejectedRoom = roomService.rejectRoom(id);
        return ResponseEntity.ok(rejectedRoom);
    }
}
