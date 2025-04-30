package one.wcy.ebookloaningtool.users;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.mapper.BorrowRecordsMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.security.PasswordEncoderService;
import one.wcy.ebookloaningtool.users.update.UpdateUserRequest;
import one.wcy.ebookloaningtool.users.update.UpdateUserResponse;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;
    private final BorrowRecordsMapper borrowRecordsMapper;
    private final BorrowService borrowService;
    @Autowired
    BookMapper bookMapper;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService, BorrowRecordsMapper borrowRecordsMapper, BorrowService borrowService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.borrowRecordsMapper = borrowRecordsMapper;
        this.borrowService = borrowService;
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
            
//            // Delete the user
//            userRepository.delete(user);
            List<BorrowList> brl =
                    borrowRecordsMapper.findBorrowList(user.getUuid(), "borrowed");
            for (BorrowList br : brl) {
                Book book = bookMapper.findBookById(br.getBookId());
                borrowService.returnBook(book, user.getUuid());
            }
            user.setActive(false);
            user.setEmail(null);
            user.setName("Inactive user");
            userRepository.save(user);
            return new Response("success");
        } catch (Exception e) {
            return new Response("error");
        }
    }
} 