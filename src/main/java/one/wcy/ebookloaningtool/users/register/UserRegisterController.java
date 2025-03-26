package one.wcy.ebookloaningtool.users.register;

import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.*;
import one.wcy.ebookloaningtool.utils.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRegisterController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

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
            user.setPassword(encodedPassword);
            // Save data
            User savedUser = userRepository.save(user);
            // Return value
            return new UserRegisterResponse(
                    "success",
                    savedUser.getUuid(),
                    savedUser.getEmail(),
                    savedUser.getName()
            );
        } else {
            return new Response(
                    "user already exists"
            );
        }
    }

}
