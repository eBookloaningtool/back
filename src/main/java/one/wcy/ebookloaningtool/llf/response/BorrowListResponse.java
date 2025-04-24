package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.utils.Response;
import java.util.List;

@Getter
@Setter
public class BorrowListResponse extends Response {
    private List<BorrowList> Data;

    public BorrowListResponse(String state, List<BorrowList> Data) {
        super(state);
        this.Data = Data;
    }
}
