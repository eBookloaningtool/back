package one.wcy.ebookloaningtool.users;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

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
    
    private BigDecimal balance; // 用户余额
    
    @Column(name = "created_at")
    private LocalDate createdat; // 创建日期
    
    @PrePersist
    protected void onCreate() {
        createdat = LocalDate.now();
    }
    
    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}
