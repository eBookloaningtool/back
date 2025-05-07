/**
 * Response class for book borrowing operations.
 * Extends the base Response class to include due date and updated balance information.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import one.wcy.ebookloaningtool.utils.Response;


@Getter
@Setter
public class BorrowResponse extends Response {
    /**
     * The due date for the borrowed book
     */
    private LocalDate dueDate;

    /**
     * The user's updated balance after the borrowing operation
     */
    private BigDecimal balance;

    /**
     * Constructs a new BorrowResponse with the specified state, due date, and balance.
     * @param state The response state indicating success or failure
     * @param localDate The due date for the borrowed book
     * @param balance The user's updated balance
     */
    public BorrowResponse(String state, LocalDate localDate, BigDecimal balance) {
        super(state);
        this.dueDate = localDate;
        this.balance = balance;
    }
}
