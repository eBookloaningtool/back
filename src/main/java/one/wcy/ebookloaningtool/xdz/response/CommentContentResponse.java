package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Response class for retrieving review content.
 * Extends the base Response class to include detailed information about a specific review.
 */
@Getter
@Setter
public class CommentContentResponse extends Response {
    /**
     * Unique identifier for the review
     */
    private String uuid;

    /**
     * ID of the book being reviewed
     */
    private String bookId;

    /**
     * Username of the reviewer
     */
    private String username;

    /**
     * Rating given to the book (e.g., 1-5 stars)
     */
    private BigDecimal rating;

    /**
     * Text content of the review
     */
    private String content;

    /**
     * Date when the review was created
     */
    private LocalDate createDate;

    /**
     * Constructs a new CommentContentResponse with the specified review details.
     * @param state The response state indicating success or failure
     * @param UUID Unique identifier for the review
     * @param bookId ID of the book being reviewed
     * @param username Username of the reviewer
     * @param rating Rating given to the book
     * @param content Text content of the review
     * @param createDate Date when the review was created
     */
    public CommentContentResponse(String state, String UUID, String bookId, String username, BigDecimal rating, String content, LocalDate createDate) {
        super(state);
        this.uuid = UUID;
        this.bookId = bookId;
        this.username = username;
        this.rating = rating;
        this.content = content;
        this.createDate = createDate;
    }
}