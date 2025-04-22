package one.wcy.ebookloaningtool.users.info;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
public class UserInfoResponse extends Response {
    private String UUID;
    private String name;
    private String email;
    private BigDecimal balance;
    private LocalDate createdat;

    public UserInfoResponse(String state, String UUID, String name, String email, BigDecimal balance, LocalDate createdat) {
        super(state);
        this.UUID = UUID;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.createdat = createdat;
    }
} 