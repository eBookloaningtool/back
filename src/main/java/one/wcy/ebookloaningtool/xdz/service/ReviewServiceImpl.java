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

    public ReviewServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Response addReview(AddReviewRequest request) {
        // 1. 参数校验
        if (request.getBookId() == null || request.getComment() == null || request.getRating() == null) {
            return new Response("Invalid parameters. Could not be null.");
        }

        if (request.getRating().compareTo(new BigDecimal("1")) < 0
                || request.getRating().compareTo(new BigDecimal("5")) > 0) {
            return new Response("Invalid rating. Rating should be between 1 and 5.");
        }

        // 2. 检查用户是否已登录
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();


        // 3. 检查书籍是否存在（调用现有BorrowService的findBookById）
        Book book = borrowService.findBookById(request.getBookId());
        if (book == null) {
            return new Response("Book not exist");
        }

        // 4. 检查用户是否已借阅该书
        if ((commentsMapper.countActiveBorrow(book.getBookId(), userId)) == 0) {
            return new Response("You haven't borrowed this book yet.");
        }


        // 5. 保存评论到数据库
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

    @Override
    public Response getCommentContent(String commentId) {
        // 参数校验
        if (commentId == null || commentId.isEmpty()) {
            return new Response("Invalid commentId.");
        }

        Comment comment = commentsMapper.selectById(commentId);
        User user = userRepository.findById(comment.getUuid())
                .orElseThrow(() -> new RuntimeException("User not found"));
        String userName = user.getName();
        if (comment == null) {
            return new Response("Comment not found");
        }

        return new CommentContentResponse("success", comment.getUuid(), userName,
                comment.getRating(), comment.getContent());
    }

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