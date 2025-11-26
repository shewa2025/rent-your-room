package rent.your.room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rent.your.room.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
