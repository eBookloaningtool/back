package one.wcy.ebookloaningtool.xdz.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a book review in the database.
 * Contains information about a review including content, rating, and timestamps.
 */
@Data
@Entity
@Table(name = "Comments")
public class Comment {
    /**
     * Unique identifier for the review
     */
    @Id
    @Column(name = "commentId")
    private String commentId;

    /**
     * Unique identifier of the user who wrote the review
     */
    @Column(name = "uuid", nullable = false)
    private String uuid;

    /**
     * ID of the book being reviewed
     */
    @Column(name = "bookId", nullable = false)
    private String bookId;

    /**
     * Text content of the review
     */
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * Date and time when the review was created
     */
    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    /**
     * Date and time when the review was last updated
     */
    @Column(name = "updateAt")
    private LocalDateTime updatedAt;

    /**
     * Rating given to the book (e.g., 1-5 stars)
     */
    @Column(name = "rating", nullable = false)
    private BigDecimal rating;
}
