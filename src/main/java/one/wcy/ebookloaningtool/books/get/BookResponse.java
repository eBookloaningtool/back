package one.wcy.ebookloaningtool.books.get;

import lombok.Data;
import one.wcy.ebookloaningtool.llf.pojo.Book;

import java.math.BigDecimal;

/**
 * Response class for book details.
 * Contains comprehensive information about a book including its metadata, availability, and statistics.
 */
@Data
public class BookResponse {
    /**
     * Unique identifier for the book
     */
    private String bookId;

    /**
     * Title of the book
     */
    private String title;

    /**
     * Author of the book
     */
    private String author;

    /**
     * Detailed description of the book
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
     * Total number of copies in the system
     */
    private int totalCopies;

    /**
     * Price of the book
     */
    private BigDecimal price;

    /**
     * Average rating of the book
     */
    private BigDecimal rating;

    /**
     * Number of times the book has been borrowed
     */
    private int borrowTimes;
    
    /**
     * Creates a BookResponse object from a Book entity.
     * @param book The Book entity to convert
     * @return A new BookResponse object containing the book's information, or null if the input is null
     */
    public static BookResponse fromBook(Book book) {
        if (book == null) {
            return null;
        }
        
        BookResponse response = new BookResponse();
        response.setBookId(book.getBookId());
        response.setTitle(book.getTitle());
        response.setAuthor(book.getAuthor());
        response.setDescription(book.getDescription());
        response.setCoverUrl(book.getCoverUrl());
        response.setCategory(book.getCategory());
        response.setAvailableCopies(book.getAvailableCopies());
        response.setTotalCopies(book.getTotalCopies());
        response.setPrice(book.getPrice());
        response.setRating(book.getRating());
        response.setBorrowTimes(book.getBorrowTimes());
        
        return response;
    }
} 