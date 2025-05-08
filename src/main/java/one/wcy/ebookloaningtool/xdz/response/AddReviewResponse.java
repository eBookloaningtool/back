package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

/**
 * Response class for adding book reviews.
 * Extends the base Response class to include the ID of the newly created review.
 */
@Getter
@Setter
public class AddReviewResponse extends Response {
    /**
     * Unique identifier for the newly created review
     */
    private String commentId;

    /**
     * Constructs a new AddReviewResponse with the specified state and comment ID.
     * @param state The response state indicating success or failure
     * @param commentId Unique identifier for the newly created review
     */
    public AddReviewResponse(String state, String commentId) {
        super(state);
        this.commentId = commentId;
    }
}
