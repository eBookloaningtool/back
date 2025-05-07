/**
 * Controller class for handling user profile update requests.
 * Provides endpoints for updating user information with JWT token validation.
 */
package one.wcy.ebookloaningtool.users.update;

import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/users")
public class UserController {

    /**
     * Service for user-related operations
     */
    private final UserService userService;

    /**
     * Service for JWT token management
     */
    private final JwtTokenService jwtTokenService;

    /**
     * Constructor for UserController with dependency injection.
     *
     * @param userService Service for user operations
     * @param jwtTokenService Service for JWT token management
     */
    @Autowired
    public UserController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Handles user profile update requests.
     * Validates the JWT token and updates the user's information.
     *
     * @param authHeader Authorization header containing the JWT token
     * @param updateRequest Request containing the fields to update
     * @return ResponseEntity containing the updated user information
     * @throws RuntimeException if the token is invalid or expired
     */
    @PostMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateUserRequest updateRequest) {
        
        // Extract and validate JWT token
        String token = authHeader.replace("Bearer ", "");
        if (!jwtTokenService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        
        // Get user UUID from token claims
        Claims claims = jwtTokenService.extractAllClaims(token);
        String uuid = claims.get("uuid", String.class);
        
        // Update user information
        UpdateUserResponse response = userService.updateUser(uuid, updateRequest);
        
        return ResponseEntity.ok(response);
    }
} 