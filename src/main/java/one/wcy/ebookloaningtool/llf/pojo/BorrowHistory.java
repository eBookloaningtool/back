package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowHistory {
    String borrowId;
    String bookId;
    LocalDate borrowDate;
    LocalDate dueDate;
    LocalDate returnDate;
    String status;
}
