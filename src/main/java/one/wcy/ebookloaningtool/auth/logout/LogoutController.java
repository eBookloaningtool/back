/**
 * Controller class handling user logout operations.
 * Provides endpoint for invalidating user authentication tokens.
 */
package one.wcy.ebookloaningtool.auth.logout;

import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;

@RestController
public class LogoutController {

    /**
     * Service for handling JWT token operations
     */
    private final JwtTokenService jwtTokenService;

    /**
     * Constructor for LogoutController with dependency injection.
     *
     * @param jwtTokenService Service for JWT token management
     */
    public LogoutController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    /**
     * Handles user logout requests.
     * Invalidates the current user's authentication token.
     * Uses ThreadLocal to retrieve the current user's claims.
     *
     * @return Response indicating the success of the logout operation
     */
    @PostMapping("/api/auth/logout")
//    public Response logout(@RequestHeader("Authorization") String authHeader) {
//        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtTokenService.validateToken(authHeader.substring(7))) {
//            String token = authHeader.substring(7);
//            jwtTokenService.invalidateToken(token);
//            return new Response("success");
//        }
//        return new Response("invalid token");
//    }
    public Response logout() {
        Claims claims = ThreadLocalUtil.get();
        jwtTokenService.invalidateToken(claims);
        return new Response("success");
    }
}
