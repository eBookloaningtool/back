package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;

/**
 * Response class for top-up operations.
 * Extends the base Response class to include payment details and updated balance.
 */
@Getter
@Setter
public class TopUpResponse extends Response {
    /**
     * The user's updated balance after the top-up operation
     */
    private BigDecimal balance;

    /**
     * Unique identifier for the payment transaction
     */
    private String paymentId;

    /**
     * Constructs a new TopUpResponse with the specified state, payment ID, and balance.
     * @param state The response state indicating success or failure
     * @param paymentId Unique identifier for the payment transaction
     * @param balance The user's updated balance
     */
    public TopUpResponse(String state, String paymentId, BigDecimal balance) {
        super(state);
        this.paymentId = paymentId;
        this.balance = balance;
    }
}
