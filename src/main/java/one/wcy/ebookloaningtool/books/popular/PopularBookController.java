package one.wcy.ebookloaningtool.books.popular;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class PopularBookController {

    @Autowired
    private BookMapper bookMapper;

    @GetMapping("/popular")
    public PopularBooksResponse getPopularBooks() {
        return new PopularBooksResponse(bookMapper.findTop5PopularBooks());
    }
} 