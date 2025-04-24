package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class BorrowFailedResponse extends Response {
    private List<String> InvalidBookIds;
    private List<String> LowStockBookIds;
    private List<String> BorrowedBookIds;
    public BorrowFailedResponse(String state, List<String> InvalidBookIds, List<String> LowStockBookIds, List<String> BorrowedBookIds) {
        super(state);
        this.InvalidBookIds = InvalidBookIds;
        this.LowStockBookIds = LowStockBookIds;
        this.BorrowedBookIds = BorrowedBookIds;
    }
}
