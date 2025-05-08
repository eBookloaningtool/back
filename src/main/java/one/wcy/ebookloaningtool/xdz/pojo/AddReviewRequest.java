package one.wcy.ebookloaningtool.xdz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request class for adding book reviews.
 * Contains the necessary information for creating a new book review.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest {
    /**
     * ID of the book being reviewed
     */
    private String bookId;

    /**
     * Rating given to the book (e.g., 1-5 stars)
     */
    private BigDecimal rating;

    /**
     * Text content of the review
     */
    private String comment;
}