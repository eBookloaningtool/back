package one.wcy.ebookloaningtool.xdz.controller;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/add")
    public Response addReview(@RequestBody AddReviewRequest request) {
        return reviewService.addReview(request);
    }

    @PostMapping("/user")
    public Response getUserReviews() {
        return reviewService.getUserReviews();
    }

    @GetMapping("/book")
    public Response getBookReviews(@RequestParam String bookId) {
        return reviewService.getBookReviews(bookId);
    }

    @GetMapping("/content")
    public Response getCommentContent(@RequestParam String commentId) {
        return reviewService.getCommentContent(commentId);
    }

    @PostMapping("/delete")
    public Response deleteReview(@RequestBody DeleteReviewRequest request) {
        return reviewService.deleteReview(request);
    }
}
