package one.wcy.ebookloaningtool.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    private String email;
    private String name;
    private String password;
    
    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
