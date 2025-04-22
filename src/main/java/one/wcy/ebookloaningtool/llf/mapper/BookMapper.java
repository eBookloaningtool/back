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

    //修改book库存（根据传入数值调整）
    @Update("update Book set availableCopies = availableCopies + #{delta} where bookId = #{id}")
    void updateAvailableCopies(String id, int delta);

    //获取借阅次数最多的前5本书
    @Select("SELECT bookId FROM Book ORDER BY borrowTimes DESC LIMIT 5")
    List<String> findTop5PopularBooks();

    //根据标题、作者、类别搜索电子书
    @Select("<script>" +
            "SELECT bookId FROM Book WHERE 1=1" +
            "<if test='title != null'> AND title LIKE CONCAT('%', #{title}, '%')</if>" +
            "<if test='author != null'> AND author LIKE CONCAT('%', #{author}, '%')</if>" +
            "<if test='category != null'> AND category LIKE CONCAT('%', #{category}, '%')</if>" +
            "</script>")
    List<String> searchBooks(String title, String author, String category);
}
