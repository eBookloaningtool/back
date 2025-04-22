package one.wcy.ebookloaningtool.users;

import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.update.UpdateUserRequest;
import one.wcy.ebookloaningtool.users.update.UpdateUserResponse;
import one.wcy.ebookloaningtool.utils.Response;
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
    
    public Response deleteUser(String uuid) {
        try {
            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Delete the user
            userRepository.delete(user);
            
            return new Response("success");
        } catch (Exception e) {
            return new Response("error");
        }
    }
} 