package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CommentsMapper {
    @Insert("INSERT INTO Comments (commentId, uuid, bookId, content, createdAt, updateAt,  rating) " +
            "VALUES (#{commentId}, #{uuid}, #{bookId}, #{content}, NOW(), NOW(), #{rating})")
    void insert(Comment comment);

    @Select("SELECT AVG(rating) FROM Comments WHERE bookId = #{bookId}")
    BigDecimal getAverageRating(String bookId);

    @Update("UPDATE Book SET rating = #{rating} WHERE bookId = #{bookId}")
    void updateRating(@Param("bookId") String bookId, @Param("rating") BigDecimal rating);

    @Select("SELECT commentId FROM Comments WHERE uuid = #{userId} ORDER BY createdAt DESC")
    List<String> findByUserId(String userId);

    @Select("SELECT commentId FROM Comments WHERE bookId = #{bookId} ORDER BY createdAt DESC")
    List<String> findByBookId(String bookId);

    @Select("SELECT * FROM Comments WHERE commentId = #{commentId}")
    Comment selectById(String commentId);

    @Delete("DELETE FROM Comments WHERE commentId = #{commentId}")
    void deleteById(String commentId);

    @Select("SELECT COUNT(*) FROM borrow_records " +
            "WHERE bookuuid = #{bookId} AND useruuid = #{userId}")
    int countActiveBorrow(@Param("bookId") String bookId, @Param("userId") String userId);
}
