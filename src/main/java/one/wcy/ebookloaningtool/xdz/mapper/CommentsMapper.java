package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.Comment;
import one.wcy.ebookloaningtool.xdz.response.CommentResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommentsMapper {
    @Insert("INSERT INTO Comments (commentUUID, userUUID, bookUUID, content, createdAt, updateAt, deleted) " +
            "VALUES (#{commentId}, #{userUUID}, #{bookUUID}, #{content}, NOW(), NOW(), 0)")
    void insert(Comment comment);

    @Select("SELECT commentUUID FROM Comments WHERE userUUID = #{userId}")
    List<String> findByUserId(String userId);

    @Select("SELECT commentUUID, content FROM Comments WHERE bookUUID = #{bookId} ORDER BY createdAt DESC")
    List<CommentResponse> findByBookId(String bookId);

    @Select("SELECT * FROM Comments WHERE commentUUID = #{commentId}")
    Comment selectById(String commentId);

    @Delete("DELETE FROM Comments WHERE commentUUID = #{commentId}")
    void deleteById(String commentId);

    @Select("SELECT COUNT(*) FROM borrow_records " +
            "WHERE bookuuid = #{bookId} AND useruuid = #{userId}")
    int countActiveBorrow(@Param("bookId") String bookId, @Param("userId") String userId);
}
