package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;


@Getter
@Setter
public class LowBalanceResponse extends Response {
    private BigDecimal newPayment;

    public LowBalanceResponse(String state, BigDecimal newPayment) {
        super(state);
        this.newPayment = newPayment;
    }
}
