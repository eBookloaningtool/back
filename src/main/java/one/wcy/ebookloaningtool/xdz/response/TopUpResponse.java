package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;


@Getter
@Setter
public class TopUpResponse extends Response {
    private BigDecimal balance;
    private String paymentId;

    public TopUpResponse(String state, String paymentId, BigDecimal balance) {
        super(state);
        this.paymentId= paymentId;
        this.balance= balance;
    }

}
