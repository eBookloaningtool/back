package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/get")
    public ResponseEntity<BookResponse> getBookDetails(@RequestParam String bookId) {
        Book book = bookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(BookResponse.fromBook(book));
    }
} 