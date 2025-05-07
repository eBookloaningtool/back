/**
 * Controller class for handling user registration requests.
 * Provides endpoints for creating new user accounts with initial settings.
 */
package one.wcy.ebookloaningtool.users.register;

import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.*;
import one.wcy.ebookloaningtool.utils.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class UserRegisterController {

    /**
     * Repository for user data access
     */
    private final UserRepository userRepository;

    /**
     * Service for password encoding and verification
     */
    private final PasswordEncoderService passwordEncoderService;

    /**
     * Initial balance for new user accounts
     */
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(0.00);

    /**
     * Constructor for UserRegisterController with dependency injection.
     *
     * @param userRepository Repository for user data access
     * @param passwordEncoderService Service for password encoding
     */
    public UserRegisterController(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    /**
     * Handles user registration requests.
     * Creates a new user account with encoded password and initial balance.
     *
     * @param user User information from the registration request
     * @return Response containing the registration result and user information if successful
     */
    @PostMapping(value = "/api/users/register")
    public Response register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) == null)
        {
            String rawPassword = user.getPassword();
            // Encode the password for secure storage
            String encodedPassword = passwordEncoderService.encodePassword(rawPassword);
            user.setEncodedPassword(encodedPassword);
            
            // Set initial balance for the new account
            user.setBalance(INITIAL_BALANCE);
            
            // Creation date will be automatically set by @PrePersist
            
            // Save the new user
            User savedUser = userRepository.save(user);
            // Return success response with user information
            return new UserRegisterResponse(
                    "success",
                    savedUser.getUuid(),
                    savedUser.getEmail(),
                    savedUser.getName(),
                    savedUser.getCreatedat()
            );
        } else {
            return new Response(
                    "user already exists"
            );
        }
    }

}
