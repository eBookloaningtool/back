package one.wcy.ebookloaningtool.xdz.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class PaymentHistoryResponse extends Response {
    private List<PaymentHistoryItem> data;

    public PaymentHistoryResponse(String state, List<PaymentHistoryItem> data) {
        super(state);
        this.data = data;
    }

    @Data
    @AllArgsConstructor
    public static class PaymentHistoryItem {
        private String paymentId;
        private String date;
        private BigDecimal amount;
    }
}