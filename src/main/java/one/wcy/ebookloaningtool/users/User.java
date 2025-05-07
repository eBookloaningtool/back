/**
 * Entity class representing a user in the system.
 * Contains user information including authentication details,
 * account status, and financial information.
 */
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

    /**
     * Unique identifier for the user
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    /**
     * User's email address, used for login and communication
     */
    private String email;

    /**
     * User's display name
     */
    private String name;

    /**
     * Encoded user password
     */
    private String password;

    /**
     * Account status: true for active, false for deactivated
     */
    private boolean active;

    /**
     * User's account balance
     */
    private BigDecimal balance;
    
    /**
     * Date when the user account was created
     */
    @Column(name = "created_at")
    private LocalDate createdat;

    /**
     * Initializes new user accounts with creation date and active status.
     * Called automatically before persisting a new user entity.
     */
    @PrePersist
    protected void onCreate() {
        createdat = LocalDate.now();
        active = true;
    }

    /**
     * Sets the encoded password for the user.
     * Used when updating or resetting the user's password.
     *
     * @param encodedPassword The encoded password string
     */
    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }
}