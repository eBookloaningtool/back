package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BorrowRecordsMapper {
    
    // 查询用户当前借阅的书籍
    @Select("SELECT * FROM BorrowRecords WHERE userUUID = #{userUUID} AND status = 'borrowed'")
    List<BorrowRecords> findBorrowedBooksByUserUUID(String userUUID);
    
    // 查询用户的历史借阅记录
    @Select("SELECT * FROM BorrowRecords WHERE userUUID = #{userUUID} AND status = 'returned'")
    List<BorrowRecords> findHistoricalBooksByUserUUID(String userUUID);
} 