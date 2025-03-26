package one.wcy.ebookloaningtool.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRegisterResponse {
    private String state;
    private String UUID;
    private String email;
    private String name;
} 