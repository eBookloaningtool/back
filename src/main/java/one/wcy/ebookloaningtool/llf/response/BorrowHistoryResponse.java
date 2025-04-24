package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.BorrowHistory;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class BorrowHistoryResponse extends Response {
    private List<BorrowHistory> Data;

    public BorrowHistoryResponse(String state, List<BorrowHistory> Data) {
        super(state);
        this.Data = Data;
    }
}
