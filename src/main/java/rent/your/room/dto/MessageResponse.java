package rent.your.room.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {
    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;

    public MessageResponse(String message) {
        this.message = message;
    }
}