package one.wcy.ebookloaningtool.xdz.controller;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/addreviews")
    public Response addReview(@RequestBody AddReviewRequest request) {
        return reviewService.addReview(request);
    }

    @PostMapping("/reviews")
    public Response getUserReviews() {
        return reviewService.getUserReviews();
    }

    @GetMapping("/{bookId}/reviews")
    public Response getBookReviews(@PathVariable String bookId) {
        return reviewService.getBookReviews(bookId);
    }

    @PostMapping("/delreviews")
    public Response deleteReview(@RequestBody DeleteReviewRequest request) {
        return reviewService.deleteReview(request);
    }
}
