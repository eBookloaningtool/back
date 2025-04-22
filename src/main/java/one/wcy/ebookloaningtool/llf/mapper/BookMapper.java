package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface BookMapper {

    //根据bookID查询电子书
    @Select("select * from Book where bookId=#{id}")
    Book findBookById(String id);

    //修改book库存（根据传入数值调整）
    @Update("update Book set availableCopies = availableCopies + #{delta} where bookId = #{id}")
    void updateAvailableCopies(String id, int delta);
}
