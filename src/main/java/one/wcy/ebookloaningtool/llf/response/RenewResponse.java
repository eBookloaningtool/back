/**
 * Response class for book renewal operations.
 * Extends the base Response class to include new due date and updated balance information.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class RenewResponse extends Response {
    /**
     * The new due date after book renewal
     */
    private LocalDate newDueDate;

    /**
     * The user's updated balance after the renewal operation
     */
    private BigDecimal balance;

    /**
     * Constructs a new RenewResponse with the specified state, new due date, and balance.
     * @param state The response state indicating success or failure
     * @param localDate The new due date after renewal
     * @param balance The user's updated balance
     */
    public RenewResponse(String state, LocalDate localDate, BigDecimal balance) {
        super(state);
        this.newDueDate = localDate;
        this.balance = balance;
    }
}