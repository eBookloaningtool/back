/**
 * Controller class handling user authentication and login operations.
 * Provides endpoints for user login and token generation.
 */
package one.wcy.ebookloaningtool.auth.login;

import one.wcy.ebookloaningtool.utils.*;
import one.wcy.ebookloaningtool.security.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import one.wcy.ebookloaningtool.users.*;

@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtTokenService jwtTokenService;

    /**
     * Constructor for LoginController with dependency injection.
     * @param userRepository Repository for user data access
     * @param passwordEncoderService Service for password encoding and verification
     * @param jwtTokenService Service for JWT token generation and management
     */
    public LoginController(UserRepository userRepository, 
                          PasswordEncoderService passwordEncoderService,
                          JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Handles user login requests.
     * Validates user credentials and generates JWT token upon successful authentication.
     * @param loginForm Login credentials containing email and password
     * @return Response object containing authentication result and token if successful
     */
    @PostMapping("/api/auth/login")
    public Response login(@RequestBody LoginForm loginForm) {
        User user = userRepository.findByEmail(loginForm.getEmail());
        if (user == null) {
            return new Response("wrong email or password");
        } else {
            if (passwordEncoderService.matches(loginForm.getPassword(), user.getPassword())) {
                String token = jwtTokenService.generateToken(user);
                return new LoginResponse("success", token, 3600, user.getUuid());
            } else {
                return new Response("wrong email or password");
            }
        }
    }
}
