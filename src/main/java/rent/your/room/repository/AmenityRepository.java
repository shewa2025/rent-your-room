package rent.your.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent.your.room.model.Amenity;

@Repository
public interface AmenityRepository extends JpaRepository<Amenity, Long> {
}
