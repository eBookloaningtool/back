package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import one.wcy.ebookloaningtool.xdz.response.PaymentHistoryResponse;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PaymentMapper {
    @Insert("INSERT INTO PaymentRecords (paymentUUID, userUUID, amount, paymentDate) " +
            "VALUES (#{paymentId}, #{userUUID}, #{amount}, NOW())")
    void insert(PaymentRecord payment);

    @Update("UPDATE user SET balance = balance + #{amount} WHERE uuid = #{userUUID}")
    void updateBalance(@Param("userUUID") String userUUID, @Param("amount") BigDecimal amount);

    @Select("SELECT paymentUUID, paymentDate, amount " +
            "FROM PaymentRecords WHERE userUUID = #{userId} ORDER BY paymentDate DESC")
    List<PaymentHistoryResponse.PaymentHistoryItem> findUserPaymentHistory(String userId);
}

