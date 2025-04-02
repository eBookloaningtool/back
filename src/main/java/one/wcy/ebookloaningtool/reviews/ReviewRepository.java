package one.wcy.ebookloaningtool.reviews;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    // Find a single review by its comment ID.
    Review findByCommentId(String commentId);

    // Find all top-level reviews for a specific book (where parentCommentId is NULL).
    List<Review> findByBookUuidAndParentCommentIdNull(String bookUuid);

    // Find child reviews by parent comment ID.
    List<Review> findByParentCommentId(String parentCommentId);

    // Find all reviews posted by a specific user.
    List<Review> findByUserUuid(String userUuid);

    //Query book reviews by pagination
    Page<Review> findByBookUuid(String bookUuid, Pageable pageable);

    // Query the total number of reviews for a specific book (including all levels).
    @Query("SELECT COUNT(r) FROM Review r WHERE r.bookUuid = :bookUuid")
    Long countByBookUuid(@Param("bookUuid") String bookUuid);

    // Query the total number of top-level reviews for a specific book (only first-level reviews).
    @Query("SELECT COUNT(r) FROM Review r WHERE r.bookUuid = :bookUuid AND r.parentCommentId IS NULL")
    Long countTopLevelReviewsByBookUuid(@Param("bookUuid") String bookUuid);

}
