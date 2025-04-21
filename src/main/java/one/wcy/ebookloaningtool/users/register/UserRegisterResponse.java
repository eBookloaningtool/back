package one.wcy.ebookloaningtool.users.register;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.*;

import java.time.LocalDate;

@Getter
public class UserRegisterResponse extends Response {
    private final String UUID;
    @Setter
    private String email;
    @Setter
    private String name;
    private LocalDate createdat;

    public UserRegisterResponse(String state, String UUID, String email, String name) {
        super(state);
        this.UUID = UUID;
        this.email = email;
        this.name = name;
    }

    public UserRegisterResponse(String state, String UUID, String email, String name, LocalDate createdat) {
        super(state);
        this.UUID = UUID;
        this.email = email;
        this.name = name;
        this.createdat = createdat;
    }
}