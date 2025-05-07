/**
 * Entity class representing a current borrowing record.
 * Contains essential information about an active book borrowing.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowList {
    /**
     * Unique identifier for the borrow record
     */
    String borrowId;

    /**
     * ID of the borrowed book
     */
    String bookId;

    /**
     * Date when the book was borrowed
     */
    LocalDate borrowDate;

    /**
     * Expected return date of the book
     */
    LocalDate dueDate;
}
