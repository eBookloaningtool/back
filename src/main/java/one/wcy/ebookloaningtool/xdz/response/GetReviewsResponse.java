package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

/**
 * Response class for retrieving book reviews.
 * Extends the base Response class to include a list of review IDs.
 */
@Getter
@Setter
public class GetReviewsResponse extends Response {
    /**
     * List of review IDs for the requested book or user
     */
    private List<String> commentIds;

    /**
     * Constructs a new GetReviewsResponse with the specified state and list of comment IDs.
     * @param state The response state indicating success or failure
     * @param commentIds List of review IDs
     */
    public GetReviewsResponse(String state, List<String> commentIds) {
        super(state);
        this.commentIds = commentIds;
    }
}