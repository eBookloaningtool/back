package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;

@Getter
@Setter
public class CommentContentResponse extends Response {
    private String UUID;
    private BigDecimal rating;
    private String content;

    public CommentContentResponse(String state, String UUID, BigDecimal rating, String content) {
            super(state);
            this.UUID = UUID;
            this.rating = rating;
            this.content = content;
    }
}