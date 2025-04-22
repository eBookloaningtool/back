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

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(0.00); // 初始余额设置为0

    public UserRegisterController(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @PostMapping(value = "/api/users/register")
    public Response register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()) == null)
        {
            String rawPassword = user.getPassword();
            // Use Hash value of the password
            String encodedPassword = passwordEncoderService.encodePassword(rawPassword);
            user.setEncodedPassword(encodedPassword);
            
            // 设置初始余额
            user.setBalance(INITIAL_BALANCE);
            
            // 创建日期将由@PrePersist自动设置
            
            // Save data
            User savedUser = userRepository.save(user);
            // Return value
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
