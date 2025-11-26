package rent.your.room.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rent.your.room.model.*;
import rent.your.room.repository.AmenityRepository;
import rent.your.room.repository.RoleRepository;
import rent.your.room.repository.RoomRepository;
import rent.your.room.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        if (userRepository.count() == 0) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();

            User user = new User("user", "user@example.com", passwordEncoder.encode("password"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            User admin = new User("admin", "admin@example.com", passwordEncoder.encode("password"));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);
        }

        if (amenityRepository.count() == 0) {
            amenityRepository.save(new Amenity("WiFi"));
            amenityRepository.save(new Amenity("Air Conditioning"));
            amenityRepository.save(new Amenity("Parking"));
            amenityRepository.save(new Amenity("Kitchen"));
        }

        if (roomRepository.count() == 0) {
            User user = userRepository.findByUsername("user").get();
            Amenity wifi = amenityRepository.findAll().get(0);
            Amenity ac = amenityRepository.findAll().get(1);

            Address address1 = new Address();
            address1.setStreet("123 Main St");
            address1.setCity("Anytown");
            address1.setState("CA");
            address1.setZipCode("12345");
            address1.setCountry("USA");

            Room room1 = new Room();
            room1.setTitle("Cozy Room in Downtown");
            room1.setDescription("A nice and cozy room in the heart of the city.");
            room1.setRent(new BigDecimal("1200.00"));
            room1.setOwner(user);
            room1.setAddress(address1);
            room1.setAmenities(Set.of(wifi, ac));
            room1.setApproved(true);
            roomRepository.save(room1);

            Address address2 = new Address();
            address2.setStreet("456 Oak Ave");
            address2.setCity("Someville");
            address2.setState("NY");
            address2.setZipCode("54321");
            address2.setCountry("USA");

            Room room2 = new Room();
            room2.setTitle("Spacious Room with a View");
            room2.setDescription("A large room with a beautiful view of the park.");
            room2.setRent(new BigDecimal("1500.00"));
            room2.setOwner(user);
            room2.setAddress(address2);
            room2.setAmenities(Set.of(wifi));
            room2.setApproved(false); // This one is pending approval
            roomRepository.save(room2);
        }
    }
}
