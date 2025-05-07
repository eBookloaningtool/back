/**
 * Response class for the list of currently borrowed books.
 * Extends the base Response class to include a list of borrow records.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.utils.Response;
import java.util.List;

@Getter
@Setter
public class BorrowListResponse extends Response {
    /**
     * List of currently borrowed books with their borrow details
     */
    private List<BorrowList> Data;

    /**
     * Constructs a new BorrowListResponse with the specified state and data.
     * @param state The response state indicating success or failure
     * @param Data The list of currently borrowed books
     */
    public BorrowListResponse(String state, List<BorrowList> Data) {
        super(state);
        this.Data = Data;
    }
}
