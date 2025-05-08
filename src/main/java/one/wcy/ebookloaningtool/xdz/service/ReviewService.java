package one.wcy.ebookloaningtool.xdz.service;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.response.CommentContentResponse;

/**
 * Service interface for managing book reviews.
 * Provides methods for adding, retrieving, and deleting book reviews.
 */
public interface ReviewService {
    /**
     * Adds a new review for a book.
     * @param request The review request containing book ID, rating, and comment
     * @return Response indicating success or failure of the operation
     */
    Response addReview(AddReviewRequest request);

    /**
     * Retrieves all reviews made by the current user.
     * @return Response containing the list of user's reviews
     */
    Response getUserReviews();

    /**
     * Retrieves all reviews for a specific book.
     * @param bookId The ID of the book
     * @return Response containing the list of book reviews
     */
    Response getBookReviews(String bookId);

    /**
     * Retrieves the content of a specific review.
     * @param commentId The ID of the review
     * @return Response containing the review content
     */
    Response getCommentContent(String commentId);

    /**
     * Deletes a specific review.
     * @param request The delete request containing the review ID
     * @return Response indicating success or failure of the operation
     */
    Response deleteReview(DeleteReviewRequest request);
}

