package one.wcy.ebookloaningtool.llf.pojo;


import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRecords {
    private String borrowId;
    private String uuid;
    private String bookId;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
}


