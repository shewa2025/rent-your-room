package rent.your.room.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
public class RoomDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal rent;
    private byte[] image;
    private boolean approved;
    private String ownerUsername;
    private AddressDto address;
    private Set<AmenityDto> amenities;
}
