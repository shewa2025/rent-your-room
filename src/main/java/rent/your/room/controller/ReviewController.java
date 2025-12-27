package rent.your.room.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rent.your.room.dto.ReviewDto;
import rent.your.room.dto.ReviewRequestDto;
import rent.your.room.model.Review;
import rent.your.room.service.ReviewService;
import rent.your.room.service.UserService;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;

    public ReviewController(ReviewService reviewService, UserService userService) {
        this.reviewService = reviewService;
        this.userService = userService;
    }

    @PostMapping
    public ReviewDto addReview(@RequestBody ReviewRequestDto reviewRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return reviewService.addReview(reviewRequestDto, username);
    }
}
