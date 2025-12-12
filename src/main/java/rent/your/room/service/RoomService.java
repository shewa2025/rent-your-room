package rent.your.room.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import rent.your.room.dto.RoomDto;
import rent.your.room.dto.RoomRequestDto;

public interface RoomService {
    RoomDto createRoom(RoomRequestDto roomRequestDto, String username, MultipartFile image);
    Page<RoomDto> getApprovedRooms(Pageable pageable);
    Page<RoomDto> searchApprovedRooms(String title, Pageable pageable);
    RoomDto getRoomById(Long id);
    Page<RoomDto> getRoomsByOwner(String username, Pageable pageable);
    Page<RoomDto> getPendingRooms(Pageable pageable);
    RoomDto approveRoom(Long id);
    RoomDto updateRoom(Long id, RoomRequestDto roomRequestDto, String username, MultipartFile image);
    void deleteRoom(Long id, String username);
}
