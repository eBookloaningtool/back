package one.wcy.ebookloaningtool.xdz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    private String bookId;
    private BigDecimal rating;
    private String comment;
}