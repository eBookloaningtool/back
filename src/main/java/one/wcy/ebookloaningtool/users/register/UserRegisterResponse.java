/**
 * Response class for user registration operations.
 * Contains the user's basic information after successful registration.
 */
package one.wcy.ebookloaningtool.users.register;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.*;

import java.time.LocalDate;

@Getter
public class UserRegisterResponse extends Response {
    /**
     * Unique identifier for the registered user
     */
    private final String UUID;

    /**
     * Email address of the registered user
     */
    @Setter
    private String email;

    /**
     * Name of the registered user
     */
    @Setter
    private String name;

    /**
     * Date when the user account was created
     */
    private LocalDate createdat;

    /**
     * Constructor for creating a basic registration response.
     *
     * @param state Status of the registration operation
     * @param UUID Unique identifier for the user
     * @param email User's email address
     * @param name User's name
     */
    public UserRegisterResponse(String state, String UUID, String email, String name) {
        super(state);
        this.UUID = UUID;
        this.email = email;
        this.name = name;
    }

    /**
     * Constructor for creating a registration response with creation date.
     *
     * @param state Status of the registration operation
     * @param UUID Unique identifier for the user
     * @param email User's email address
     * @param name User's name
     * @param createdat Date when the user account was created
     */
    public UserRegisterResponse(String state, String UUID, String email, String name, LocalDate createdat) {
        super(state);
        this.UUID = UUID;
        this.email = email;
        this.name = name;
        this.createdat = createdat;
    }
}