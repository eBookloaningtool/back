package one.wcy.ebookloaningtool.books.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Response object for book content requests.
 * Contains the book identifier and either the content URL or an error message.
 */
@Getter
@Setter
public class BookContentResponse {
    /**
     * The unique identifier of the book.
     */
    private String bookId;

    /**
     * The URL to access the book's content or an error message if the book hasn't been borrowed.
     */
    private String contentURL;

    /**
     * Constructs a new BookContentResponse with the specified book ID and content URL.
     *
     * @param bookId The unique identifier of the book
     * @param contentURL The URL to access the book's content or an error message
     */
    public BookContentResponse(String bookId, String contentURL) {
        this.bookId = bookId;
        this.contentURL = contentURL;
    }
} 