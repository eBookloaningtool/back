package one.wcy.ebookloaningtool.users.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    private String state;
    private String UUID;
    private String email;
    private String name;
} 