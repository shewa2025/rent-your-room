package rent.your.room.dto;

import lombok.Data;

@Data
public class ReviewDto {
    private Long id;
    private String comment;
    private int rating;
    private String username;
}
