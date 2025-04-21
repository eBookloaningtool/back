package one.wcy.ebookloaningtool.xdz.service;

import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.xdz.pojo.AddReviewRequest;
import one.wcy.ebookloaningtool.xdz.pojo.DeleteReviewRequest;

public interface ReviewService {
    Response addReview(AddReviewRequest request);

    Response getUserReviews();

    Response getBookReviews(String bookId);

    Response deleteReview(DeleteReviewRequest request);

}

