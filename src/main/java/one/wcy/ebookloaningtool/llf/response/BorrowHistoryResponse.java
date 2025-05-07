/**
 * Response class for retrieving user's borrowing history.
 * Extends the base Response class to include a list of historical borrow records.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.BorrowHistory;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class BorrowHistoryResponse extends Response {
    /**
     * List of historical borrow records with complete borrow details
     */
    private List<BorrowHistory> Data;

    /**
     * Constructs a new BorrowHistoryResponse with the specified state and history data.
     * @param state The response state indicating success or failure
     * @param Data The list of historical borrow records
     */
    public BorrowHistoryResponse(String state, List<BorrowHistory> Data) {
        super(state);
        this.Data = Data;
    }
}
