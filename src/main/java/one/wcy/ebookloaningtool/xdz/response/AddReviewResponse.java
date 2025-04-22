package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

@Getter
@Setter
public class AddReviewResponse extends Response {
    private String commentId;
    public AddReviewResponse(String state, String commentId) {
        super(state);
        this.commentId = commentId;
    }
}
