package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import java.util.List;

/**
 * Service interface for managing e-books.
 * Provides methods for retrieving book details and searching books.
 */
public interface BookService {
    
    /**
     * Retrieves an e-book by its ID.
     * @param bookId The ID of the e-book
     * @return The e-book object if found, null otherwise
     */
    Book getBookById(String bookId);
    
    /**
     * Searches for e-books by title, author, and category.
     * @param title Title keyword
     * @param author Author keyword
     * @param category Category keyword
     * @return List of matching e-book IDs
     */
    List<String> searchBooks(String title, String author, String category);
} 