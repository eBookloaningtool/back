package one.wcy.ebookloaningtool.reviews;
import one.wcy.ebookloaningtool.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // Submit a review
    @PostMapping("/books/{bookId}")
    public ResponseEntity<?> submitReview(
            @PathVariable String bookId,
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false, defaultValue = "1") Integer rating,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String parentId) {

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Comment content cannot be empty.");
        }

        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5.");
        }

        Review review = reviewService.submitReview(
                currentUser.getUuid(),
                bookId,
                rating,
                content,
                parentId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    // Get the review tree of a book.
    @GetMapping("/books/{bookId}")
    public ResponseEntity<?> getBookReviewTree(
            @PathVariable String bookId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        if (page < 0 || size < 1) {
            return ResponseEntity.badRequest().body("Pagination parameters are invalid.");
        }

        Page<Review> reviews = reviewService.getBookReviewsByPage(bookId, page, size);
        return ResponseEntity.ok(reviews);
    }

    // Delete a review.
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
            @PathVariable String reviewId,
            @AuthenticationPrincipal User currentUser) {

        // Check if the review exists.
        if (!reviewService.reviewExists(reviewId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Review does not exist.");
        }

        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // Get user's review history
    @GetMapping("/users/{userId}")
    public ResponseEntity<?> getUserReviews(
            @PathVariable String userId) {

        List<Review> reviews = reviewService.getUserReviews(userId);
        return ResponseEntity.ok(reviews);
    }
}