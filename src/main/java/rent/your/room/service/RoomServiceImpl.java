package rent.your.room.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import rent.your.room.dto.AddressDto;
import rent.your.room.dto.AmenityDto;
import rent.your.room.dto.RoomDto;
import rent.your.room.dto.RoomRequestDto;
import rent.your.room.model.*;
import rent.your.room.repository.AmenityRepository;
import rent.your.room.repository.RoomRepository;
import rent.your.room.repository.UserRepository;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Override
    @Transactional
    public RoomDto createRoom(RoomRequestDto roomRequestDto, String username, MultipartFile image) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setStreet(roomRequestDto.getAddress().getStreet());
        address.setCity(roomRequestDto.getAddress().getCity());
        address.setState(roomRequestDto.getAddress().getState());
        address.setZipCode(roomRequestDto.getAddress().getZipCode());
        address.setCountry(roomRequestDto.getAddress().getCountry());

        Set<Amenity> amenities = new HashSet<>();
        if (roomRequestDto.getAmenityIds() != null) {
            amenities = roomRequestDto.getAmenityIds().stream()
                    .map(id -> amenityRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Amenity not found")))
                    .collect(Collectors.toSet());
        }

        Room room = new Room();
        room.setTitle(roomRequestDto.getTitle());
        room.setDescription(roomRequestDto.getDescription());
        room.setRent(roomRequestDto.getRent());
        try {
            room.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Could not store the image. Error: " + e.getMessage());
        }
        room.setOwner(owner);
        room.setAddress(address);
        room.setAmenities(amenities);
        room.setApproved(false); // Rooms are not approved by default

        Room savedRoom = roomRepository.save(room);
        return convertToDto(savedRoom);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getApprovedRooms(Pageable pageable) {
        return roomRepository.findByApproved(true, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> searchApprovedRooms(String title, Pageable pageable) {
        return roomRepository.findByTitleContainingAndApproved(title, true, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return convertToDto(room);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getRoomsByOwner(String username, Pageable pageable) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return roomRepository.findByOwner(owner, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RoomDto> getPendingRooms(Pageable pageable) {
        return roomRepository.findByApproved(false, pageable).map(this::convertToDto);
    }

    @Override
    @Transactional
    public RoomDto approveRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.setApproved(true);
        Room updatedRoom = roomRepository.save(room);
        return convertToDto(updatedRoom);
    }

    @Override
    @Transactional
    public RoomDto updateRoom(Long id, RoomRequestDto roomRequestDto, String username, MultipartFile image) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!room.getOwner().equals(currentUser)) {
            throw new AccessDeniedException("You are not the owner of this room");
        }

        room.setTitle(roomRequestDto.getTitle());
        room.setDescription(roomRequestDto.getDescription());
        room.setRent(roomRequestDto.getRent());

        if (image != null && !image.isEmpty()) {
            try {
                room.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Could not store the image. Error: " + e.getMessage());
            }
        }

        Address address = room.getAddress();
        address.setStreet(roomRequestDto.getAddress().getStreet());
        address.setCity(roomRequestDto.getAddress().getCity());
        address.setState(roomRequestDto.getAddress().getState());
        address.setZipCode(roomRequestDto.getAddress().getZipCode());
        address.setCountry(roomRequestDto.getAddress().getCountry());
        room.setAddress(address);

        if (roomRequestDto.getAmenityIds() != null) {
            Set<Amenity> amenities = roomRequestDto.getAmenityIds().stream()
                    .map(amenityId -> amenityRepository.findById(amenityId)
                            .orElseThrow(() -> new RuntimeException("Amenity not found")))
                    .collect(Collectors.toSet());
            room.setAmenities(amenities);
        }

        Room updatedRoom = roomRepository.save(room);
        return convertToDto(updatedRoom);
    }

    @Override
    @Transactional
    public void deleteRoom(Long id, String username) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!room.getOwner().equals(currentUser)) {
            throw new AccessDeniedException("You are not the owner of this room");
        }
        roomRepository.delete(room);
    }

    private RoomDto convertToDto(Room room) {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setTitle(room.getTitle());
        roomDto.setDescription(room.getDescription());
        roomDto.setRent(room.getRent());
        roomDto.setImage(room.getImage());
        roomDto.setApproved(room.isApproved());
        roomDto.setOwnerUsername(room.getOwner().getUsername());

        AddressDto addressDto = new AddressDto();
        Address address = room.getAddress();
        addressDto.setId(address.getId());
        addressDto.setStreet(address.getStreet());
        addressDto.setCity(address.getCity());
        addressDto.setState(address.getState());
        addressDto.setZipCode(address.getZipCode());
        addressDto.setCountry(address.getCountry());
        roomDto.setAddress(addressDto);

        if (room.getAmenities() != null) {
            roomDto.setAmenities(room.getAmenities().stream().map(amenity -> {
                AmenityDto amenityDto = new AmenityDto();
                amenityDto.setId(amenity.getId());
                amenityDto.setName(amenity.getName());
                return amenityDto;
            }).collect(Collectors.toSet()));
        }
        return roomDto;
    }
}
