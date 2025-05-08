package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper interface for Comment entity.
 * Provides database operations for managing book reviews and ratings.
 */
@Mapper
public interface CommentsMapper {
    /**
     * Inserts a new comment into the database.
     * @param comment The comment to be inserted
     */
    @Insert("INSERT INTO Comments (commentId, uuid, bookId, content, createdAt, updateAt,  rating) " +
            "VALUES (#{commentId}, #{uuid}, #{bookId}, #{content}, NOW(), NOW(), #{rating})")
    void insert(Comment comment);

    /**
     * Calculates the average rating for a specific book.
     * @param bookId The unique identifier of the book
     * @return The average rating value
     */
    @Select("SELECT AVG(rating) FROM Comments WHERE bookId = #{bookId}")
    BigDecimal getAverageRating(String bookId);

    /**
     * Updates the rating of a book in the database.
     * @param bookId The unique identifier of the book
     * @param rating The new rating value
     */
    @Update("UPDATE Book SET rating = #{rating} WHERE bookId = #{bookId}")
    void updateRating(@Param("bookId") String bookId, @Param("rating") BigDecimal rating);

    /**
     * Retrieves all comment IDs for a specific user.
     * @param userId The unique identifier of the user
     * @return List of comment IDs ordered by creation date
     */
    @Select("SELECT commentId FROM Comments WHERE uuid = #{userId} ORDER BY createdAt DESC")
    List<String> findByUserId(String userId);

    /**
     * Retrieves all comment IDs for a specific book.
     * @param bookId The unique identifier of the book
     * @return List of comment IDs ordered by creation date
     */
    @Select("SELECT commentId FROM Comments WHERE bookId = #{bookId} ORDER BY createdAt DESC")
    List<String> findByBookId(String bookId);

    /**
     * Retrieves a specific comment by its ID.
     * @param commentId The unique identifier of the comment
     * @return The comment entity if found, null otherwise
     */
    @Select("SELECT * FROM Comments WHERE commentId = #{commentId}")
    Comment selectById(String commentId);

    /**
     * Deletes a specific comment from the database.
     * @param commentId The unique identifier of the comment to delete
     */
    @Delete("DELETE FROM Comments WHERE commentId = #{commentId}")
    void deleteById(String commentId);

    /**
     * Counts the number of active borrows for a specific book and user.
     * @param bookId The unique identifier of the book
     * @param userId The unique identifier of the user
     * @return The number of active borrows
     */
    @Select("SELECT COUNT(*) FROM BorrowRecords " +
            "WHERE bookId = #{bookId} AND uuid = #{userId}")
    int countActiveBorrow(@Param("bookId") String bookId, @Param("userId") String userId);
}
