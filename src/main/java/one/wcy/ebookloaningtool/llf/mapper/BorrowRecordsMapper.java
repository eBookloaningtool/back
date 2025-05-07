/**
 * Mapper interface for BorrowRecords entity.
 * Provides database operations for managing book borrowing records,
 * including tracking current borrows, history, and record updates.
 */
package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.BorrowHistory;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BorrowRecordsMapper {
    /**
     * Retrieves borrow records by book ID, user ID, and status.
     * @param bookUUID The unique identifier of the book
     * @param userUUID The unique identifier of the user
     * @param status The status of the borrow record
     * @return List of matching borrow records
     */
    @Select("select * from BorrowRecords where bookId = #{bookUUID} and uuid = #{userUUID} and status = #{status}")
    List<BorrowRecords> findByBookUUIDAndUserUUIDAndStatus(String bookUUID, String userUUID, String status);

    /**
     * Retrieves all borrow records with a specific status.
     * @param status The status to filter by
     * @return List of borrow records with the specified status
     */
    @Select("select * from BorrowRecords where status = #{status} ORDER BY borrowDate")
    List<BorrowRecords> findByStatus(String status);

    /**
     * Retrieves a simplified list of borrow records for a user.
     * @param userUUID The unique identifier of the user
     * @param status The status of the borrow records
     * @return List of borrow records with basic information
     */
    @Select("select borrowId,bookId,borrowDate,dueDate from BorrowRecords where uuid = #{userUUID} and status = #{status} ORDER BY borrowDate")
    List<BorrowList> findBorrowList(String userUUID, String status);

    /**
     * Retrieves the complete borrowing history for a user.
     * @param userUUID The unique identifier of the user
     * @return List of borrow records including return dates and status
     */
    @Select("select borrowId,bookId,borrowDate,dueDate, returnDate, status from BorrowRecords where uuid = #{userUUID} ORDER BY borrowDate")
    List<BorrowHistory> findBorrowHistory(String userUUID);

    /**
     * Creates a new borrow record in the database.
     * @param borrowRecords The borrow record entity to be inserted
     */
    @Insert("insert into BorrowRecords (borrowId, uuid, bookId, borrowDate, dueDate, returnDate, status) " +
            "values (UUID(), #{uuid}, #{bookId}, #{borrowDate}, #{dueDate}, #{returnDate}, #{status})")
    void addBorrowRecord(BorrowRecords borrowRecords);

    /**
     * Updates an existing borrow record in the database.
     * @param borrowRecords The borrow record entity containing updated information
     * @return Number of rows affected by the update operation
     */
    @Update("update BorrowRecords " +
            "set uuid = #{uuid}, " +
            "bookId = #{bookId}, " +
            "borrowDate = #{borrowDate}, " +
            "dueDate = #{dueDate}, " +
            "returnDate = #{returnDate}, " +
            "status = #{status} " +
            "Where borrowId = #{borrowId}")
    int updateBorrowRecord(BorrowRecords borrowRecords);

    /**
     * Counts the number of borrow records for a user with a specific status.
     * @param userId The unique identifier of the user
     * @param status The status to count
     * @return The number of matching borrow records
     */
    @Select("SELECT COUNT(*) FROM BorrowRecords " +
            "WHERE uuid = #{userId} and status = #{status}")
    int countBorrow(String userId, String status);
}
