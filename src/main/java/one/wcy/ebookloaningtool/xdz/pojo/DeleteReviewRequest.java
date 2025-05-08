package one.wcy.ebookloaningtool.xdz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request class for deleting book reviews.
 * Contains the ID of the review to be deleted.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReviewRequest {
    /**
     * Unique identifier of the review to be deleted
     */
    private String commentId;
}