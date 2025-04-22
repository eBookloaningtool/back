package one.wcy.ebookloaningtool.xdz.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;


@Getter
@Setter
public class TopUpResponse extends Response {
    private double balance;
    private String paymentId;

    public TopUpResponse(String state, String paymentId, double balance) {
        super(state);
        this.paymentId= paymentId;
        this.balance= balance;
    }

}
