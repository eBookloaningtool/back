package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import one.wcy.ebookloaningtool.xdz.response.PaymentHistoryResponse;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Mapper interface for PaymentRecord entity.
 * Provides database operations for managing payment records and user balances.
 */
@Mapper
public interface PaymentMapper {
    /**
     * Inserts a new payment record into the database.
     * @param payment The payment record to be inserted
     */
    @Insert("INSERT INTO PaymentRecords (paymentId, uuid, amount, paymentDate) " +
            "VALUES (#{paymentId}, #{uuid}, #{amount}, NOW())")
    void insert(PaymentRecord payment);

    /**
     * Updates a user's balance by adding the specified amount.
     * @param uuid The unique identifier of the user
     * @param amount The amount to be added to the user's balance
     */
    @Update("UPDATE user SET balance = balance + #{amount} WHERE uuid = #{uuid}")
    void updateBalance(@Param("uuid") String uuid, @Param("amount") BigDecimal amount);

    /**
     * Retrieves the payment history for a specific user.
     * @param userId The unique identifier of the user
     * @return List of payment history items ordered by payment date
     */
    @Select("SELECT paymentId, paymentDate, amount " +
            "FROM PaymentRecords WHERE uuid = #{userId} ORDER BY paymentDate DESC")
    List<PaymentHistoryResponse.PaymentHistoryItem> findUserPaymentHistory(String userId);
}

