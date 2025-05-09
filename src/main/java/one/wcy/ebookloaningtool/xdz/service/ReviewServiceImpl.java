package one.wcy.ebookloaningtool.xdz.service;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import one.wcy.ebookloaningtool.xdz.mapper.CommentsMapper;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.response.AddReviewResponse;
import one.wcy.ebookloaningtool.xdz.response.CommentContentResponse;
import one.wcy.ebookloaningtool.xdz.response.GetReviewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of the review service interface.
 * Handles operations related to book reviews, including adding, retrieving, and deleting reviews.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private BorrowService borrowService;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private final UserRepository userRepository;

    /**
     * Constructs a new ReviewServiceImpl with the specified user repository.
     *
     * @param userRepository The repository for user-related operations
     */
    public ReviewServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Adds a new review for a book.
     *
     * @param request The review request containing book ID, rating, and comment
     * @return Response containing the generated comment ID or an error message
     */
    @Override
    public Response addReview(AddReviewRequest request) {
        // 1. Parameter validation
        if (request.getBookId() == null || request.getComment() == null || request.getRating() == null) {
            return new Response("Invalid parameters. Could not be null.");
        }

        if (request.getRating().compareTo(new BigDecimal("1")) < 0
                || request.getRating().compareTo(new BigDecimal("5")) > 0) {
            return new Response("Invalid rating. Rating should be between 1 and 5.");
        }

        // 2. Check if user is logged in
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        // 3. Check if book exists (using existing BorrowService's findBookById)
        Book book = borrowService.findBookById(request.getBookId());
        if (book == null) {
            return new Response("Book not exist");
        }

        // 4. Check if user has borrowed the book
        if ((commentsMapper.countActiveBorrow(book.getBookId(), userId)) == 0) {
            return new Response("You haven't borrowed this book yet.");
        }

        // 5. Save comment to database
        Comment comment = new Comment();
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setUuid(userId);
        comment.setBookId(request.getBookId());
        comment.setContent(request.getComment());
        comment.setRating(request.getRating());
        commentsMapper.insert(comment);

        BigDecimal avgRating = commentsMapper.getAverageRating(request.getBookId());
        commentsMapper.updateRating(request.getBookId(), avgRating);

        String generatedCommentId = comment.getCommentId();

        return new AddReviewResponse("success", generatedCommentId);
    }

    /**
     * Retrieves all reviews made by the current user.
     *
     * @return Response containing the list of user's review IDs
     */
    @Override
    public Response getUserReviews() {
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        List<String> commentIds = commentsMapper.findByUserId(userId);

        if (commentIds.isEmpty()) {
            return new Response("No valid comments.");
        }

        return new GetReviewsResponse("success", commentIds);
    }

    /**
     * Retrieves all reviews for a specific book.
     *
     * @param bookId The unique identifier of the book
     * @return Response containing the list of review IDs for the specified book
     */
    @Override
    public Response getBookReviews(String bookId) {
        if (bookId == null) {
            return new Response("Book ID is required");
        }

        if (bookMapper.findBookById(bookId) == null) {
            return new Response("Invalid book.");
        }

        List<String> commentIds = commentsMapper.findByBookId(bookId);

        if (commentIds.isEmpty()) {
            return new Response("No valid comments.");
        }

        return new GetReviewsResponse("success", commentIds);
    }

    /**
     * Retrieves the content of a specific review.
     *
     * @param commentId The unique identifier of the review
     * @return Response containing the review content, including user information and rating
     */
    @Override
    public Response getCommentContent(String commentId) {
        // Parameter validation
        if (commentId == null || commentId.isEmpty()) {
            return new Response("Invalid commentId.");
        }

        Comment comment = commentsMapper.selectById(commentId);
        String bookId = comment.getBookId();
        User user = userRepository.findById(comment.getUuid())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String userName = user.getName();
        if (comment == null) {
            return new Response("Comment not found");
        }

        return new CommentContentResponse("success", comment.getUuid(), bookId, userName,
                comment.getRating(), comment.getContent(), comment.getCreatedAt().toLocalDate());
    }

    /**
     * Deletes a specific review.
     *
     * @param request The delete request containing the review ID
     * @return Response indicating the success or failure of the deletion
     */
    @Override
    public Response deleteReview(DeleteReviewRequest request) {
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        String commentId = request.getCommentId();
        Comment comment = commentsMapper.selectById(commentId);
        if (comment == null) {
            return new Response("Comment not found: " + commentId);
        }
        if (!comment.getUuid().equals(userId)) {
            return new Response("You can't delete others' comments");
        }

        commentsMapper.deleteById(commentId);

        BigDecimal avgRating = commentsMapper.getAverageRating(comment.getBookId());
        commentsMapper.updateRating(comment.getBookId(), avgRating);

        return new Response("success");
    }

}