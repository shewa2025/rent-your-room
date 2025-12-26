package rent.your.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rent.your.room.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
