package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowList {
    String borrowId;
    String bookId;
    LocalDate borrowDate;
    LocalDate dueDate;
}
