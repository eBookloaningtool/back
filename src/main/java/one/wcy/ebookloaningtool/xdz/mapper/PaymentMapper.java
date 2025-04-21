package one.wcy.ebookloaningtool.xdz.mapper;

import one.wcy.ebookloaningtool.xdz.pojo.PaymentRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface PaymentMapper {
    @Insert("INSERT INTO PaymentRecords (userUUID, amount, paymentDate, status, method) " +
            "VALUES (#{userUUID}, #{amount}, NOW(), 'COMPLETED', 'HorsePay')")
    void insert(PaymentRecord payment);

    @Update("UPDATE users SET balance = balance + #{amount} WHERE uuid = #{userUUID}")
    void updateBalance(@Param("userUUID") String userUUID, @Param("amount") BigDecimal amount);
}
