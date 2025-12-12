package rent.your.room.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rent.your.room.dto.RoomDto;
import rent.your.room.dto.RoomRequestDto;
import rent.your.room.service.RoomService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RoomDto> createRoom(
            @Valid @RequestPart("room") RoomRequestDto roomRequestDto,
            @RequestParam("image") MultipartFile image
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RoomDto createdRoom = roomService.createRoom(roomRequestDto, username, image);
        return ResponseEntity.ok(createdRoom);
    }

    @GetMapping("/my-rooms")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Page<RoomDto>> getMyRooms(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Page<RoomDto> rooms = roomService.getRoomsByOwner(username, pageable);
        return ResponseEntity.ok(rooms);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RoomDto> updateRoom(
            @PathVariable Long id,
            @Valid @RequestPart("room") RoomRequestDto roomRequestDto,
            @RequestParam("image") MultipartFile image
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        RoomDto updatedRoom = roomService.updateRoom(id, roomRequestDto, username, image);
        return ResponseEntity.ok(updatedRoom);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoom(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        roomService.deleteRoom(id, username);
        return ResponseEntity.ok().build();
    }
}
