package one.wcy.ebookloaningtool.xdz.controller;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling book review operations.
 * Provides endpoints for adding, retrieving, and deleting book reviews.
 */
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    /**
     * Adds a new review for a book.
     *
     * @param request The review request containing book ID, rating, and comment
     * @return Response indicating the success or failure of the operation
     */
    @PostMapping("/add")
    public Response addReview(@RequestBody AddReviewRequest request) {
        return reviewService.addReview(request);
    }

    /**
     * Retrieves all reviews made by the current user.
     *
     * @return Response containing the list of user's reviews
     */
    @PostMapping("/user")
    public Response getUserReviews() {
        return reviewService.getUserReviews();
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The unique identifier of the book
     * @return Response containing the list of reviews for the specified book
     */
    @GetMapping("/book")
    public Response getBookReviews(@RequestParam String bookId) {
        return reviewService.getBookReviews(bookId);
    }

    /**
     * Retrieves the content of a specific review.
     *
     * @param commentId The unique identifier of the review
     * @return Response containing the review content
     */
    @GetMapping("/content")
    public Response getCommentContent(@RequestParam String commentId) {
        return reviewService.getCommentContent(commentId);
    }

    /**
     * Deletes a specific review.
     *
     * @param request The delete request containing the review ID
     * @return Response indicating the success or failure of the deletion
     */
    @PostMapping("/delete")
    public Response deleteReview(@RequestBody DeleteReviewRequest request) {
        return reviewService.deleteReview(request);
    }
}
