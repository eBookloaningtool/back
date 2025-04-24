package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetWishlistResponse extends Response {
    private List<String> bookId;
    public GetWishlistResponse(String state, List<String> bookId) {
        super(state);
        this.bookId = bookId;
    }
}
