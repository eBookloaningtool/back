package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CommentsMapper {
    @Insert("INSERT INTO Comments (commentUUID, userUUID, bookUUID, content, createdAt, updateAt,  rating) " +
            "VALUES (#{commentId}, #{userUUID}, #{bookUUID}, #{content}, NOW(), NOW(), #{rating})")
    void insert(Comment comment);

    @Select("SELECT AVG(rating) FROM Comments WHERE bookUUID = #{bookId}")
    BigDecimal getAverageRating(String bookId);

    @Update("UPDATE book SET rating = #{rating} WHERE bookId = #{bookId}")
    void updateRating(@Param("bookId") String bookId, @Param("rating") BigDecimal rating);

    @Select("SELECT commentUUID FROM Comments WHERE userUUID = #{userId} ORDER BY createdAt DESC")
    List<String> findByUserId(String userId);

    @Select("SELECT commentUUID FROM Comments WHERE bookUUID = #{bookId} ORDER BY createdAt DESC")
    List<String> findByBookId(String bookId);

    @Select("SELECT * FROM Comments WHERE commentUUID = #{commentId}")
    Comment selectById(String commentId);

    @Delete("DELETE FROM Comments WHERE commentUUID = #{commentId}")
    void deleteById(String commentId);

    @Select("SELECT COUNT(*) FROM borrow_records " +
            "WHERE bookuuid = #{bookId} AND useruuid = #{userId}")
    int countActiveBorrow(@Param("bookId") String bookId, @Param("userId") String userId);
}
