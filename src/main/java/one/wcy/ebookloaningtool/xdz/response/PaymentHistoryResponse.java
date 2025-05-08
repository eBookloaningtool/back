package one.wcy.ebookloaningtool.xdz.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response class for retrieving payment history.
 * Extends the base Response class to include a list of payment history items.
 */
@Getter
@Setter
public class PaymentHistoryResponse extends Response {
    /**
     * List of payment history items
     */
    private List<PaymentHistoryItem> data;

    /**
     * Constructs a new PaymentHistoryResponse with the specified state and data.
     * @param state The response state indicating success or failure
     * @param data List of payment history items
     */
    public PaymentHistoryResponse(String state, List<PaymentHistoryItem> data) {
        super(state);
        this.data = data;
    }

    /**
     * Inner class representing a single payment history item.
     * Contains details about a specific payment transaction.
     */
    @Data
    @AllArgsConstructor
    public static class PaymentHistoryItem {
        /**
         * Unique identifier for the payment transaction
         */
        private String paymentId;

        /**
         * Date when the payment was made
         */
        private String date;

        /**
         * Amount of the payment
         */
        private BigDecimal amount;
    }
}