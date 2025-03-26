package one.wcy.ebookloaningtool.auth.login;

import one.wcy.ebookloaningtool.utils.*;
import one.wcy.ebookloaningtool.security.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import one.wcy.ebookloaningtool.users.*;

@RestController
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final JwtTokenService jwtTokenService;

    public LoginController(UserRepository userRepository, 
                          PasswordEncoderService passwordEncoderService,
                          JwtTokenService jwtTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping("/api/auth/login")
    public Response login(@RequestBody LoginForm loginForm) {
        User user = userRepository.findByEmail(loginForm.getEmail());
        if (user == null) {
            return new Response("wrong email or password");
        } else {
            if (passwordEncoderService.matches(loginForm.getPassword(), user.getPassword())) {
                String token = jwtTokenService.generateToken(user);
                return new LoginResponse("success", token, 3600, user.getUuid());
            } else {
                return new Response("wrong email or password");
            }
        }
    }
}
