/**
 * Response class for user information retrieval operations.
 * Contains comprehensive user profile information including account details and balance.
 */
package one.wcy.ebookloaningtool.users.info;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

import java.time.LocalDate;
import java.math.BigDecimal;

@Getter
@Setter
public class UserInfoResponse extends Response {
    /**
     * Unique identifier for the user
     */
    private String UUID;

    /**
     * User's display name
     */
    private String name;

    /**
     * User's email address
     */
    private String email;

    /**
     * User's current account balance
     */
    private BigDecimal balance;

    /**
     * Date when the user account was created
     */
    private LocalDate createdat;

    /**
     * Constructor for creating a user information response.
     *
     * @param state Status of the information retrieval operation
     * @param UUID Unique identifier for the user
     * @param name User's display name
     * @param email User's email address
     * @param balance User's current account balance
     * @param createdat Date when the user account was created
     */
    public UserInfoResponse(String state, String UUID, String name, String email, BigDecimal balance, LocalDate createdat) {
        super(state);
        this.UUID = UUID;
        this.name = name;
        this.email = email;
        this.balance = balance;
        this.createdat = createdat;
    }
} 