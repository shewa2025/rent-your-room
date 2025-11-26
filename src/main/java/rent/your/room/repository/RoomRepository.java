package rent.your.room.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent.your.room.model.Room;
import rent.your.room.model.User;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    Page<Room> findByApproved(boolean approved, Pageable pageable);

    Page<Room> findByOwner(User owner, Pageable pageable);

    Page<Room> findByTitleContainingAndApproved(String title, boolean approved, Pageable pageable);
}
