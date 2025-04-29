package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper {

    //根据bookID查询电子书
    @Select("select * from Book where bookId=#{id}")
    Book findBookById(String id);
    //根据提供的Book更新实体
    @Update("update Book " +
            "set title = #{title}, " +
            "author = #{author}, " +
            "description = #{description}, " +
            "coverUrl = #{coverUrl}, " +
            "availableCopies = #{availableCopies}, " +
            "price = #{price}, " +
            "rating = #{rating}, " +
            "category = #{category}, " +
            "contentURL = #{contentURL}, " +
            "borrowTimes = #{borrowTimes}, " +
            "totalCopies = #{totalCopies} " +
            "Where bookId = #{bookId}")
    int updateBook(Book book);

    //按借阅次数降序获取热门书籍
    @Select("SELECT bookId FROM Book ORDER BY borrowTimes DESC")
    List<String> findTopPopularBooks();

    //根据标题、作者、类别搜索电子书
    @Select("<script>" +
            "SELECT bookId FROM Book WHERE 1=1" +
            "<if test='title != null'> AND title LIKE CONCAT('%', #{title}, '%')</if>" +
            "<if test='author != null'> AND author LIKE CONCAT('%', #{author}, '%')</if>" +
            "<if test='category != null'> AND category LIKE CONCAT('%', #{category}, '%')</if>" +
            "</script>")
    List<String> searchBooks(String title, String author, String category);
}
