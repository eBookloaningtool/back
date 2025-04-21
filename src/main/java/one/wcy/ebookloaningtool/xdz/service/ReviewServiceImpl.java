package one.wcy.ebookloaningtool.xdz.service;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import one.wcy.ebookloaningtool.xdz.mapper.CommentsMapper;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.response.AddReviewResponse;
import one.wcy.ebookloaningtool.xdz.response.CommentResponse;
import one.wcy.ebookloaningtool.xdz.response.GetReviewsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private BorrowService borrowService;

    @Override
    public Response addReview(AddReviewRequest request) {
        // 1. 参数校验
        if (request.getBookId() == null || request.getRating() < 1 || request.getRating() > 5) {
            return new Response("Invalid parameters");
        }

        // 2. 检查用户是否已登录
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();


        // 3. 检查书籍是否存在（调用现有BorrowService的findBookById）
        Books book = borrowService.findBookById(request.getBookId());
        if (book == null) {
            return new Response("Book not exist");
        }

        // 4. 检查用户是否已借阅该书
//        if (borrowService.checkBorrow(book.getBookId(), userId).isEmpty()) {
//            return new Response("You haven't borrowed this book");
//        }

        if ((commentsMapper.countActiveBorrow(book.getBookId(), userId))>0) {
            return new Response("You haven't borrowed this book");
        }



        // 5. 保存评论到数据库
        Comment comment = new Comment();
        comment.setCommentId(UUID.randomUUID().toString());
        comment.setUserUUID(userId);
        comment.setBookUUID(request.getBookId());
        comment.setContent(request.getComment());
        commentsMapper.insert(comment);

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
//        List<String> commentIds = comments.stream()
//                .map(Comment::getCommentId)
//                .collect(Collectors.toList());

        return new GetReviewsResponse("commentId", commentIds);
    }

    @Override
    public Response getBookReviews(String bookId) {
        if (bookId == null) {
            return new Response("Book ID is required");
        }

        List<CommentResponse> comments = commentsMapper.findByBookId(bookId);
        List<CommentResponse> responseList = comments.stream()
                .map(comment -> new CommentResponse(
                        comment.getCommentId(),
                        comment.getContent()))
                .collect(Collectors.toList());
        List<String> commentIds=responseList.stream()
                .map(CommentResponse::getCommentId)
                .collect(Collectors.toList());

        return new GetReviewsResponse("success", commentIds);
    }

    @Override
    public Response deleteReview(DeleteReviewRequest request) {
        Claims claims = ThreadLocalUtil.get();
        if (claims == null) {
            return new Response("Unauthorized");
        }
        String userId = claims.get("uuid").toString();

        for (String commentId : request.getCommentId()) {
            Comment comment = commentsMapper.selectById(commentId);
            if (comment == null) {
                return new Response("Comment not found: " + commentId);
            }
            if (!comment.getUserUUID().equals(userId)) {
                return new Response("You can't delete others' comments");
            }
            commentsMapper.deleteById(commentId);
        }

        return new Response("success");
    }

}