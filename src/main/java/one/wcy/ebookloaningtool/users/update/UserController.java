package one.wcy.ebookloaningtool.users.update;

import one.wcy.ebookloaningtool.security.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenService jwtTokenService;

    @Autowired
    public UserController(UserService userService, JwtTokenService jwtTokenService) {
        this.userService = userService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody UpdateUserRequest updateRequest) {
        
        // 提取并验证JWT令牌
        String token = authHeader.replace("Bearer ", "");
        if (!jwtTokenService.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        
        // 从令牌中获取用户UUID
        Claims claims = jwtTokenService.extractAllClaims(token);
        String uuid = claims.get("uuid", String.class);
        
        // 更新用户信息
        UpdateUserResponse response = userService.updateUser(uuid, updateRequest);
        
        return ResponseEntity.ok(response);
    }
} 