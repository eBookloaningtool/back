/**
 * Service class for managing user operations.
 * Handles user profile updates, account deletion, and related operations.
 */
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

    /**
     * Repository for user data access
     */
    private final UserRepository userRepository;

    /**
     * Service for password encoding and verification
     */
    private final PasswordEncoderService passwordEncoderService;

    /**
     * Mapper for accessing borrow records
     */
    private final BorrowRecordsMapper borrowRecordsMapper;

    /**
     * Service for handling book borrowing operations
     */
    private final BorrowService borrowService;

    /**
     * Mapper for accessing book data
     */
    @Autowired
    BookMapper bookMapper;

    /**
     * Constructor for UserService with dependency injection.
     *
     * @param userRepository Repository for user data access
     * @param passwordEncoderService Service for password encoding
     * @param borrowRecordsMapper Mapper for borrow records
     * @param borrowService Service for book borrowing operations
     */
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoderService passwordEncoderService, BorrowRecordsMapper borrowRecordsMapper, BorrowService borrowService) {
        this.userRepository = userRepository;
        this.passwordEncoderService = passwordEncoderService;
        this.borrowRecordsMapper = borrowRecordsMapper;
        this.borrowService = borrowService;
    }

    /**
     * Updates a user's profile information.
     * Can update email, name, and password.
     *
     * @param uuid UUID of the user to update
     * @param updateRequest Request containing the fields to update
     * @return Response containing the updated user information
     * @throws RuntimeException if user is not found
     */
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
    
    /**
     * Deletes a user account.
     * Instead of physically deleting the user, it deactivates the account
     * and returns any borrowed books.
     *
     * @param uuid UUID of the user to delete
     * @return Response indicating the success or failure of the operation
     */
    public Response deleteUser(String uuid) {
        try {
            User user = userRepository.findById(uuid)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            // Return all borrowed books
            List<BorrowList> brl = borrowRecordsMapper.findBorrowList(user.getUuid(), "borrowed");
            for (BorrowList br : brl) {
                Book book = bookMapper.findBookById(br.getBookId());
                borrowService.returnBook(book, user.getUuid());
            }

            // Deactivate user account
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