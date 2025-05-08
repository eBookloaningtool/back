package one.wcy.ebookloaningtool.books.search;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

/**
 * Response class for book search operations.
 * Extends the base Response class to include a list of book IDs matching the search criteria.
 */
@Getter
@Setter
public class SearchBooksResponse extends Response {
    /**
     * List of book IDs that match the search criteria
     */
    private List<String> bookId;

    /**
     * Constructs a new SearchBooksResponse with the specified state and search results.
     * @param state The response state indicating success or failure
     * @param bookIds List of book IDs matching the search criteria
     */
    public SearchBooksResponse(String state, List<String> bookIds) {
        super(state);
        this.bookId = bookIds;
    }
    
    /**
     * Constructs a new SearchBooksResponse with only a state.
     * Used when no search results are found or when an error occurs.
     * @param state The response state indicating success or failure
     */
    public SearchBooksResponse(String state) {
        super(state);
    }
} 