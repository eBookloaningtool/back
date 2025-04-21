package one.wcy.ebookloaningtool.users.delete;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")

public class DeleteController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public DeleteController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteUserResponse> deleteUser(
            @RequestHeader("Authorization") String authHeader) {

        // 提取并验证JWT令牌
        String token = authHeader.replace("Bearer ", "");
        if (!jwtTokenService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        // 从令牌中获取用户UUID
        Claims claims = jwtTokenService.extractAllClaims(token);
        String uuid = claims.get("uuid", String.class);

        // 删除用户
        DeleteUserResponse response = userService.deleteUser(uuid);

        // 使该用户的所有令牌失效
        if (response.getState().equals("success")) {
            jwtTokenService.invalidateAllUserTokens(uuid);
            jwtTokenService.invalidateToken(claims);
        }

        return ResponseEntity.ok(response);
    }
}
