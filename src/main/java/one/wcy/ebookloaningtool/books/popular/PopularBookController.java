package one.wcy.ebookloaningtool.books.popular;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for retrieving popular books.
 * Provides endpoints for accessing the most popular books in the system.
 */
@RestController
@RequestMapping("/api/books")
public class PopularBookController {

    @Autowired
    private BookMapper bookMapper;

    /**
     * Retrieves a list of the most popular books.
     * @return PopularBooksResponse containing the list of popular books
     */
    @GetMapping("/popular")
    public PopularBooksResponse getPopularBooks() {
        return new PopularBooksResponse(bookMapper.findTopPopularBooks());
    }
} 