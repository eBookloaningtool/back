/**
 * Request class for book-related operations.
 * Used to encapsulate a list of book IDs for batch operations.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BooksRequest {
    /**
     * List of book IDs to be processed in the request
     */
    private List<String> bookId;
}
