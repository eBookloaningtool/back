package one.wcy.ebookloaningtool.books;

import lombok.Data;
import one.wcy.ebookloaningtool.llf.pojo.Book;

import java.math.BigDecimal;

@Data
public class BookResponse {
    private String bookId;
    private String title;
    private String author;
    private String description;
    private String coverUrl;
    private String category;
    private int availableCopies;
    private int totalCopies;
    private BigDecimal price;
    private BigDecimal rating;
    private int borrowTimes;
    
    // 从Book对象构造BookResponse对象
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