package one.wcy.ebookloaningtool.auth.logout;

import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@RestController
public class LogoutController {

    private final JwtTokenService jwtTokenService;

    public LogoutController(JwtTokenService jwtTokenService) {
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/api/auth/logout")
    public Response logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ") && jwtTokenService.validateToken(authHeader.substring(7))) {
            String token = authHeader.substring(7);
            jwtTokenService.invalidateToken(token);
            return new Response("success");
        }
        return new Response("invalid token");
    }
}
