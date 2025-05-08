package one.wcy.ebookloaningtool.books.search;

import one.wcy.ebookloaningtool.books.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller class for searching books.
 * Provides endpoints for searching books by various criteria such as title, author, and category.
 */
@RestController
@RequestMapping("/api/books")
public class SearchController {

    @Autowired
    private BookService bookService;

    /**
     * Searches for books based on provided criteria.
     * At least one search parameter (title, author, or category) must be provided.
     * @param title The title of the book to search for (optional)
     * @param author The author of the book to search for (optional)
     * @param category The category of the book to search for (optional)
     * @return ResponseEntity containing the search results or an error response if no search criteria provided
     */
    @GetMapping("/search")
    public ResponseEntity<SearchBooksResponse> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {
        // Check if at least one search criteria is provided
        if (title == null && author == null && category == null) {
            return ResponseEntity.badRequest().body(new SearchBooksResponse("error"));
        }

        List<String> bookIds = bookService.searchBooks(title, author, category);
        return ResponseEntity.ok(new SearchBooksResponse("success", bookIds));
    }
}
