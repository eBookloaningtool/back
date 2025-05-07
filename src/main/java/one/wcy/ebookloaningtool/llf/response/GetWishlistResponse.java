/**
 * Response class for retrieving user's wishlist contents.
 * Extends the base Response class to include a list of book IDs in the wishlist.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetWishlistResponse extends Response {
    /**
     * List of book IDs currently in the user's wishlist
     */
    private List<String> bookId;

    /**
     * Constructs a new GetWishlistResponse with the specified state and wishlist contents.
     * @param state The response state indicating success or failure
     * @param bookId The list of book IDs in the user's wishlist
     */
    public GetWishlistResponse(String state, List<String> bookId) {
        super(state);
        this.bookId = bookId;
    }
}
