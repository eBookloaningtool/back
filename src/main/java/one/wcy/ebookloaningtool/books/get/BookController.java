package one.wcy.ebookloaningtool.books.get;

import one.wcy.ebookloaningtool.books.BookService;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for retrieving book details.
 * Provides endpoints for accessing book information.
 */
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    /**
     * Retrieves detailed information about a specific book.
     * @param bookId The unique identifier of the book
     * @return ResponseEntity containing the book details if found, or a 404 response if not found
     */
    @GetMapping("/get")
    public ResponseEntity<BookResponse> getBookDetails(@RequestParam String bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookResponse.fromBook(book));
    }
} 