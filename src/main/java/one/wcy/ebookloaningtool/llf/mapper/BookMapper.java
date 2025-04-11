package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.Books;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.awt.print.Book;

@Mapper
public interface BookMapper {

    //根据bookID查询电子书
    @Select("select * from Books where bookId=#{id}")
    Books findBookById(String id);

    //修改book库存（根据传入数值调整）
    @Update("update Books set stock = stock + #{delta} where bookId = #{id}")
    void updateStock(String id, int delta);
}
