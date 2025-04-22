package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetReviewsResponse extends Response {
    private List<String> commentIds;

    public GetReviewsResponse(String state, List<String> commentIds) {
            super(state);
            this.commentIds = commentIds;
    }
}