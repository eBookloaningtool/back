package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import one.wcy.ebookloaningtool.xdz.response.PaymentHistoryResponse;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface PaymentMapper {
    @Insert("INSERT INTO PaymentRecords (paymentId, uuid, amount, paymentDate) " +
            "VALUES (#{paymentId}, #{uuid}, #{amount}, NOW())")
    void insert(PaymentRecord payment);

    @Update("UPDATE user SET balance = balance + #{amount} WHERE uuid = #{uuid}")
    void updateBalance(@Param("uuid") String uuid, @Param("amount") BigDecimal amount);

    @Select("SELECT paymentId, paymentDate, amount " +
            "FROM PaymentRecords WHERE uuid = #{userId} ORDER BY paymentDate DESC")
    List<PaymentHistoryResponse.PaymentHistoryItem> findUserPaymentHistory(String userId);
}

