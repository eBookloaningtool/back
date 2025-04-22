package one.wcy.ebookloaningtool.xdz.service;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;
import one.wcy.ebookloaningtool.xdz.response.CommentContentResponse;

public interface ReviewService {
    Response addReview(AddReviewRequest request);

    Response getUserReviews();

    Response getBookReviews(String bookId);

    Response getCommentContent(String commentId);

    Response deleteReview(DeleteReviewRequest request);

}

