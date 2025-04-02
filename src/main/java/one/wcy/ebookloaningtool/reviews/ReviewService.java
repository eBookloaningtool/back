package one.wcy.ebookloaningtool.reviews;

import jakarta.transaction.Transactional;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    // Submit a review (supports multi-level replies)
    @Transactional
    public Review submitReview(String userId, String bookId, Integer rating, String content, String parentId) {
        // 1. Validate if the user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User does not exist."));

        // 2. Validate if the rating is valid (must be between 1 and 5)
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }

        // 3. Create a review object
        Review review = new Review();
        review.setUserUuid(userId);
        review.setBookUuid(bookId);
        review.setRating(rating);
        review.setContent(content);
        review.setParentCommentId(parentId); // Set parent comment ID

        // 4. Root comment ID logic:
        // Root comment's root_id equals its own ID；
        if (parentId == null) {
            review.setRootCommentId(review.getCommentId());
        } else {
            // Child comments inherit their parent's root_id
            Review parentReview = reviewRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent comment does not exist."));
            review.setRootCommentId(parentReview.getRootCommentId());
        }

        // 5. Save to database
        return reviewRepository.save(review);
    }

    // Build the tree structure.
    private List<Review> buildTree(List<Review> rootReviews) {
        return rootReviews.stream()
                .peek(root -> root.setChildren(fetchChildren(root.getCommentId())))
                .collect(Collectors.toList());
    }

    // Get all child comments.
    private List<Review> fetchChildren(String parentId) {
        List<Review> children = reviewRepository.findByParentCommentId(parentId);
        children.forEach(child -> child.setChildren(fetchChildren(child.getCommentId())));

        return children;
    }

    // Get the review tree of a book.
    public List<Review> getBookReviewTree(String bookId) {
        List<Review> rootReviews = reviewRepository.findByBookUuidAndParentCommentIdNull(bookId);

        return buildTree(rootReviews);
    }

    // Delete a review.
    @Transactional
    public void deleteReview(String reviewId) {
        // Check if the review exists.
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review does not exist."));

        reviewRepository.delete(review);
    }

    // Get user review history.
    public List<Review> getUserReviews(String userId) {
        return reviewRepository.findByUserUuid(userId);
    }

    // Query book reviews by pagination
    public Page<Review> getBookReviewsByPage(String bookId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return reviewRepository.findByBookUuid(bookId, pageable);
    }

    public boolean reviewExists(String reviewId) {
        return reviewRepository.existsById(reviewId);
    }
}
