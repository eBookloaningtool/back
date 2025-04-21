package one.wcy.ebookloaningtool.xdz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    private String bookId;
    private int rating;
    private String comment;
}