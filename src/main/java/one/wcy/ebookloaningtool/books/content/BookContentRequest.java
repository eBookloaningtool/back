package one.wcy.ebookloaningtool.books.content;

import lombok.Getter;
import lombok.Setter;

/**
 * Request object for retrieving book content.
 * Contains the necessary information to identify the book.
 */
@Getter
@Setter
public class BookContentRequest {
    /**
     * The unique identifier of the book.
     */
    private String bookId;
} 