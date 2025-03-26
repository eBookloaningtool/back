package one.wcy.ebookloaningtool.auth.login;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

@Getter
@Setter
public class LoginResponse extends Response {
    private String token;
    private int expiresIn;
    private String UUID;

    public LoginResponse(String state, String token, int expiresIn, String UUID) {
        super(state);
        this.token = token;
        this.expiresIn = expiresIn;
        this.UUID = UUID;
    }
} 