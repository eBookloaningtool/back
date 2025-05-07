/**
 * Controller class for handling user information retrieval requests.
 * Provides endpoints for accessing user profile information.
 */
package one.wcy.ebookloaningtool.users.info;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserInfoController {

    /**
     * Repository for user data access
     */
    private final UserRepository userRepository;

    /**
     * Constructor for UserInfoController with dependency injection.
     *
     * @param userRepository Repository for user data access
     */
    @Autowired
    public UserInfoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Handles user information retrieval requests.
     * Retrieves and returns the complete profile information for the authenticated user.
     *
     * @return ResponseEntity containing the user's profile information or an error response
     */
    @PostMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        // Get user claims from ThreadLocal
        Claims claims = ThreadLocalUtil.get();
        String uuid = claims.get("uuid", String.class);

        // Retrieve user information
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            return ResponseEntity.badRequest().body(new UserInfoResponse("user not found", null, null, null, null, null));
        }

        // Return user information
        return ResponseEntity.ok(new UserInfoResponse(
                "success",
                user.getUuid(),
                user.getName(),
                user.getEmail(),
                user.getBalance(),
                user.getCreatedat()
        ));
    }
} 