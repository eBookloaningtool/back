package one.wcy.ebookloaningtool.users.update;

import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
    }

    public UpdateUserResponse updateUser(String uuid, UpdateUserRequest updateRequest) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (updateRequest.getEmail() != null) {
            user.setEmail(updateRequest.getEmail());
        }

        if (updateRequest.getName() != null) {
            user.setName(updateRequest.getName());
        }

        if (updateRequest.getPassword() != null) {
            String encodedPassword = passwordEncoderService.encodePassword(updateRequest.getPassword());
            user.setEncodedPassword(encodedPassword);
        }

        User updatedUser = userRepository.save(user);

        return new UpdateUserResponse(
                "success",
                updatedUser.getUuid(),
                updatedUser.getEmail(),
                updatedUser.getName()
        );
    }
} 