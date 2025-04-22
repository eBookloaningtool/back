package one.wcy.ebookloaningtool.users.delete;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUserResponse {
    private String state;

    public DeleteUserResponse(String state) {
        this.state = state;
    }
} 