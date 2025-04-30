package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;

@Getter
@Setter
public class CommentContentResponse extends Response {
    private String UUID;
    private String username;
    private BigDecimal rating;
    private String content;

    public CommentContentResponse(String state, String UUID, String username, BigDecimal rating, String content) {
            super(state);
            this.UUID = UUID;
            this.username = username;
            this.rating = rating;
            this.content = content;
    }
}