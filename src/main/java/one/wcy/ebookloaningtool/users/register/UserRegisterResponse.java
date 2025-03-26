package one.wcy.ebookloaningtool.users.register;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.*;

@Getter
public class UserRegisterResponse extends Response {
    private final String UUID;
    @Setter
    private String email;
    @Setter
    private String name;

    public UserRegisterResponse(String state, String UUID, String email, String name) {
        super(state);
        this.UUID = UUID;
        this.email = email;
        this.name = name;
    }


}