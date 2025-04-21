package one.wcy.ebookloaningtool.xdz.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponse {
    private String commentId;
    private String content;
}