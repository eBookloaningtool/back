package one.wcy.ebookloaningtool.users;

import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    public UserController(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    @PostMapping(value = "/api/users/register")
    public UserRegisterResponse register(@RequestBody User user) {
        String rawPassword = user.getPassword();
        String encodedPassword = passwordEncoderService.encodePassword(rawPassword);
        user.setPassword(encodedPassword);

        User savedUser = userRepository.save(user);
        
        return new UserRegisterResponse(
            "success", 
            savedUser.getUuid(), 
            savedUser.getEmail(), 
            savedUser.getName()
        );
    }

}
