package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;
    
    @Override
    public Book getBookById(String bookId) {
        return bookMapper.findBookById(bookId);
    }
    
    @Override
    public List<String> searchBooks(String title, String author, String category) {
        return bookMapper.searchBooks(title, author, category);
    }
} 