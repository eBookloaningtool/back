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

    private final UserRepository userRepository;

    @Autowired
    public UserInfoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {
        // 从ThreadLocal获取claims
        Claims claims = ThreadLocalUtil.get();
        String uuid = claims.get("uuid", String.class);

        // 获取用户信息
        User user = userRepository.findByUuid(uuid);
        if (user == null) {
            return ResponseEntity.badRequest().body(new UserInfoResponse("user not found", null, null, null, null, null));
        }

        // 返回用户信息
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