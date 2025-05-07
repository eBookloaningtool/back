/**
 * Data transfer object (DTO) for login requests.
 * Contains the necessary fields for user authentication.
 */
package one.wcy.ebookloaningtool.auth.login;

import lombok.Data;

@Data
public class LoginForm {
    /**
     * User's email address used for login
     */
    private String email;
    
    /**
     * User's password for authentication
     */
    private String password;
}
