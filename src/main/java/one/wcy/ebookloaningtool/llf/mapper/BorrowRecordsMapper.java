package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.BorrowHistory;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BorrowRecordsMapper {
    //根据userid,bookid,status查找记录,可能存在多条，用List存储
    @Select("select * from BorrowRecords where bookId = #{bookUUID} and uuid = #{userUUID} and status = #{status}")
    List<BorrowRecords> findByBookUUIDAndUserUUIDAndStatus(String bookUUID, String userUUID, String status);

    //根据status查找记录，可能存在多条，用List存储
    @Select("select * from BorrowRecords where status = #{status} ORDER BY borrowDate")
    List<BorrowRecords> findByStatus(String status);

    //根据用户id和status查找借阅记录
    @Select("select borrowId,bookId,borrowDate,dueDate from BorrowRecords where uuid = #{userUUID} and status = #{status} ORDER BY borrowDate")
    List<BorrowList> findBorrowList(String userUUID, String status);

    //根据用户id查找借阅历史
    @Select("select borrowId,bookId,borrowDate,dueDate, returnDate, status from BorrowRecords where uuid = #{userUUID} ORDER BY borrowDate")
    List<BorrowHistory> findBorrowHistory(String userUUID);

    //根据提供的BorrowRecords实体插入
    @Insert("insert into BorrowRecords (borrowId, uuid, bookId, borrowDate, dueDate, returnDate, status) " +
            "values (UUID(), #{uuid}, #{bookId}, #{borrowDate}, #{dueDate}, #{returnDate}, #{status})")
    void addBorrowRecord(BorrowRecords borrowRecords);

    //根据提供的BorrowRecords更新实体
    @Update("update BorrowRecords " +
            "set uuid = #{uuid}, " +
            "bookId = #{bookId}, " +
            "borrowDate = #{borrowDate}, " +
            "dueDate = #{dueDate}, " +
            "returnDate = #{returnDate}, " +
            "status = #{status} " +
            "Where borrowId = #{borrowId}")
    int updateBorrowRecord(BorrowRecords borrowRecords);

    @Select("SELECT COUNT(*) FROM BorrowRecords " +
            "WHERE uuid = #{userId} and status = #{status}")
    int countBorrow(String userId, String status);


}
