/**
 * Response object for successful login operations.
 * Extends the base Response class to include authentication token and related information.
 */
package one.wcy.ebookloaningtool.auth.login;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.utils.Response;

@Getter
@Setter
public class LoginResponse extends Response {
    /**
     * JWT token for authenticated user session
     */
    private String token;
    
    /**
     * Token expiration time in seconds
     */
    private int expiresIn;
    
    /**
     * Unique identifier for the authenticated user
     */
    private String UUID;

    /**
     * Constructor for creating a login response.
     * @param state Status message of the login operation
     * @param token JWT token for authentication
     * @param expiresIn Token expiration time in seconds
     * @param UUID User's unique identifier
     */
    public LoginResponse(String state, String token, int expiresIn, String UUID) {
        super(state);
        this.token = token;
        this.expiresIn = expiresIn;
        this.UUID = UUID;
    }
} 