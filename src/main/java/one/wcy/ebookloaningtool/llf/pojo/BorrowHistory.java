/**
 * Entity class representing a book borrowing history record.
 * Contains information about a book's borrowing status and timeline.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowHistory {
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

    /**
     * Actual date when the book was returned
     */
    LocalDate returnDate;

    /**
     * Current status of the borrowing (e.g., BORROWED, RETURNED, OVERDUE)
     */
    String status;
}
