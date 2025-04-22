package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class RenewResponse extends Response {
    private LocalDate newDueDate;
    private BigDecimal balance;
    public RenewResponse(String state, LocalDate localDate, BigDecimal balance) {
        super(state);
        this.newDueDate = localDate;
        this.balance = balance;
    }

}