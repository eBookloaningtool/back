package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the BookService interface.
 * Provides concrete implementation for managing e-books,
 * including book retrieval and search operations.
 */
@Service
public class BookServiceImpl implements BookService {

    /**
     * Mapper for database operations related to books
     */
    @Autowired
    private BookMapper bookMapper;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Book getBookById(String bookId) {
        return bookMapper.findBookById(bookId);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> searchBooks(String title, String author, String category) {
        return bookMapper.searchBooks(title, author, category);
    }
} 