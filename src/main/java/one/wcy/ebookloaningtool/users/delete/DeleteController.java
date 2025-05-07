/**
 * Controller class for handling user account deletion requests.
 * Provides endpoints for deactivating user accounts and invalidating their tokens.
 */
package one.wcy.ebookloaningtool.users.delete;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.users.UserService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class DeleteController {

    /**
     * Service for user-related operations
     */
    private final UserService userService;

    /**
     * Service for JWT token management
     */
    private final JwtTokenService jwtTokenService;

    /**
     * Constructor for DeleteController with dependency injection.
     *
     * @param userService Service for user operations
     * @param jwtTokenService Service for JWT token management
     */
    @Autowired
    public DeleteController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Handles user account deletion requests.
     * Deactivates the user account and invalidates all associated tokens.
     *
     * @return ResponseEntity containing the deletion operation result
     */
    @PostMapping("/delete")
    public ResponseEntity<Response> deleteUser() {
        // Get user claims from ThreadLocal
        Claims claims = ThreadLocalUtil.get();
        String uuid = claims.get("uuid", String.class);

        // Delete the user account
        Response response = userService.deleteUser(uuid);

        // Invalidate all tokens for the user if deletion was successful
        if (response.getState().equals("success")) {
            jwtTokenService.invalidateAllUserTokens(uuid);
            jwtTokenService.invalidateToken(claims);
        }

        return ResponseEntity.ok(response);
    }
}
