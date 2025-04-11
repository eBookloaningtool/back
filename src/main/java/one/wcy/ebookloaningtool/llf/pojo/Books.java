package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Books {
    private String bookId;
    private String title;
    private String author;
    private String description;
    private String cover;
    private int stock;
    private BigDecimal price;
    private int borrowDuration;
    private String comments;
    private BigDecimal rating;
}
