/**
 * Response class for insufficient balance scenarios.
 * Extends the base Response class to include the additional payment amount required.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;


@Getter
@Setter
public class LowBalanceResponse extends Response {
    /**
     * The additional payment amount required to complete the operation
     */
    private BigDecimal newPayment;

    /**
     * Constructs a new LowBalanceResponse with the specified state and required payment.
     * @param state The response state indicating insufficient balance
     * @param newPayment The additional payment amount required
     */
    public LowBalanceResponse(String state, BigDecimal newPayment) {
        super(state);
        this.newPayment = newPayment;
    }
}
