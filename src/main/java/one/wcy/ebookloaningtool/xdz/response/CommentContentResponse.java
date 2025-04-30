package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;

@Getter
@Setter
public class CommentContentResponse extends Response {
    private String uuid;
    private String bookId;
    private String username;
    private BigDecimal rating;
    private String content;

    public CommentContentResponse(String state, String UUID, String bookId, String username, BigDecimal rating, String content) {
            super(state);
            this.uuid = UUID;
            this.bookId = bookId;
            this.username = username;
            this.rating = rating;
            this.content = content;
    }
}