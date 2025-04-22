package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Book {
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
    private String contentURL;
}
