
package rent.your.room.dto;

import lombok.Data;

@Data
public class ReviewRequestDto {
    private Long roomId;
    private String comment;
    private int rating;
}
