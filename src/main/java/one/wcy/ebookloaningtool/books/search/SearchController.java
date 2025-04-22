package one.wcy.ebookloaningtool.books.search;

import one.wcy.ebookloaningtool.books.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class SearchController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<SearchBooksResponse> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String category) {
        // 检查是否至少提供了一个搜索条件
        if (title == null && author == null && category == null) {
            return ResponseEntity.badRequest().body(new SearchBooksResponse("error"));
        }

        List<String> bookIds = bookService.searchBooks(title, author, category);
        return ResponseEntity.ok(new SearchBooksResponse("success", bookIds));
    }
}
