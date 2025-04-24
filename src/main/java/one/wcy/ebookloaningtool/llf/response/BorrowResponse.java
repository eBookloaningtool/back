package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

import one.wcy.ebookloaningtool.utils.Response;


@Getter
@Setter
public class BorrowResponse extends Response {
    private LocalDate dueDate;
    private BigDecimal balance;

    public BorrowResponse(String state, LocalDate localDate, BigDecimal balance) {
        super(state);
        this.dueDate = localDate;
        this.balance = balance;
    }
}
