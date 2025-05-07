/**
 * Entity class representing a book borrowing record in the database.
 * Contains complete information about a book borrowing transaction,
 * including user identification and borrowing status.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BorrowRecords {
    /**
     * Unique identifier for the borrow record
     */
    private String borrowId;

    /**
     * Unique identifier of the user who borrowed the book
     */
    private String uuid;

    /**
     * ID of the borrowed book
     */
    private String bookId;

    /**
     * Date when the book was borrowed
     */
    private LocalDate borrowDate;

    /**
     * Expected return date of the book
     */
    private LocalDate dueDate;

    /**
     * Actual date when the book was returned
     */
    private LocalDate returnDate;

    /**
     * Current status of the borrowing (e.g., BORROWED, RETURNED, OVERDUE)
     */
    private String status;
}


