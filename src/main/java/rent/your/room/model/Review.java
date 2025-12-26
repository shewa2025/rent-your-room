package rent.your.room.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue
    private Long id;

    private String comment;
    private int rating;

    @ManyToOne
    private User user;

    @ManyToOne
    private Room room;
}
