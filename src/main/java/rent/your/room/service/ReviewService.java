
package rent.your.room.service;

import org.springframework.stereotype.Service;
import rent.your.room.dto.ReviewDto;
import rent.your.room.dto.ReviewRequestDto;
import rent.your.room.model.Review;
import rent.your.room.model.Room;
import rent.your.room.model.User;
import rent.your.room.repository.ReviewRepository;
import rent.your.room.repository.RoomRepository;
import rent.your.room.repository.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
    }

    public ReviewDto addReview(ReviewRequestDto reviewRequestDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Room room = roomRepository.findById(reviewRequestDto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Review review = new Review();
        review.setComment(reviewRequestDto.getComment());
        review.setRating(reviewRequestDto.getRating());
        review.setUser(user);
        review.setRoom(room);

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setId(review.getId());
        reviewDto.setComment(review.getComment());
        reviewDto.setRating(review.getRating());
        reviewDto.setUsername(review.getUser().getUsername());
        return reviewDto;
    }
}

