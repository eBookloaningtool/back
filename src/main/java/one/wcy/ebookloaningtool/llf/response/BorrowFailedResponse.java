/**
 * Response class for failed book borrowing operations.
 * Extends the base Response class to include details about why the borrowing failed,
 * categorizing books into invalid, low stock, or already borrowed.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class BorrowFailedResponse extends Response {
    /**
     * List of book IDs that do not exist in the system
     */
    private List<String> InvalidBookIds;

    /**
     * List of book IDs that have insufficient available copies
     */
    private List<String> LowStockBookIds;

    /**
     * List of book IDs that are already borrowed by the user
     */
    private List<String> BorrowedBookIds;

    /**
     * Constructs a new BorrowFailedResponse with the specified state and failure details.
     * @param state The response state indicating failure
     * @param InvalidBookIds List of invalid book IDs
     * @param LowStockBookIds List of books with insufficient stock
     * @param BorrowedBookIds List of books already borrowed by the user
     */
    public BorrowFailedResponse(String state, List<String> InvalidBookIds, List<String> LowStockBookIds, List<String> BorrowedBookIds) {
        super(state);
        this.InvalidBookIds = InvalidBookIds;
        this.LowStockBookIds = LowStockBookIds;
        this.BorrowedBookIds = BorrowedBookIds;
    }
}
