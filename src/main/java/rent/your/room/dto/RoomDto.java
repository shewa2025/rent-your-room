package rent.your.room.dto;

import lombok.Data;
import rent.your.room.model.Address;
import rent.your.room.model.Amenity;
import rent.your.room.model.User;

import java.util.List;

@Data
public class RoomDto {
    private Long id;
    private String name;
    private String description;
    private int price;
    private String type;
    private Address address;
    private List<Amenity> amenities;
    private User user;
}
