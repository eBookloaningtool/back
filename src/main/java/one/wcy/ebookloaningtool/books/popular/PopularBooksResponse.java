package one.wcy.ebookloaningtool.books.popular;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

/**
 * Response class for popular books.
 * Extends the base Response class to include a list of popular book IDs.
 */
@Getter
@Setter
public class PopularBooksResponse extends Response {
    /**
     * List of book IDs ordered by popularity
     */
    private List<String> bookId;

    /**
     * Constructs a new PopularBooksResponse with the list of popular book IDs.
     * @param bookIds The list of book IDs ordered by popularity
     */
    public PopularBooksResponse(List<String> bookIds) {
        super("success");
        this.bookId = bookIds;
    }
} 