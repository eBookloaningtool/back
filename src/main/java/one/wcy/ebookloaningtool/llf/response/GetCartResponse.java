/**
 * Response class for retrieving user's shopping cart contents.
 * Extends the base Response class to include a list of book IDs in the cart.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetCartResponse extends Response {
    /**
     * List of book IDs currently in the user's shopping cart
     */
    private List<String> bookId;

    /**
     * Constructs a new GetCartResponse with the specified state and cart contents.
     * @param state The response state indicating success or failure
     * @param bookId The list of book IDs in the user's cart
     */
    public GetCartResponse(String state, List<String> bookId) {
        super(state);
        this.bookId = bookId;
    }
}
