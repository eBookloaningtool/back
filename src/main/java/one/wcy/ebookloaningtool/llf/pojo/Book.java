/**
 * Entity class representing a book in the system.
 * Contains comprehensive information about a book's details,
 * availability, and metadata.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Book {
    /**
     * Unique identifier for the book
     */
    private String bookId;

    /**
     * Title of the book
     */
    private String title;

    /**
     * Author(s) of the book
     */
    private String author;

    /**
     * Detailed description of the book's content
     */
    private String description;

    /**
     * URL to the book's cover image
     */
    private String coverUrl;

    /**
     * Category or genre of the book
     */
    private String category;

    /**
     * Number of copies currently available for borrowing
     */
    private int availableCopies;

    /**
     * Total number of copies in the library's collection
     */
    private int totalCopies;

    /**
     * Price of the book
     */
    private BigDecimal price;

    /**
     * Average rating of the book (e.g., 1-5 stars)
     */
    private BigDecimal rating;

    /**
     * Number of times the book has been borrowed
     */
    private int borrowTimes;

    /**
     * URL to access the book's content
     */
    private String contentURL;
}
