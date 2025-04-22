package one.wcy.ebookloaningtool.xdz.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "PaymentRecords")
public class PaymentRecord {
    @Id
    @Column(name = "paymentUUID")
    private String paymentId; // 主键

    @Column(name = "userUUID", nullable = false)
    private String userUUID; // 外键关联Users表

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paymentDate", nullable = false)
    private LocalDateTime paymentDate;

}
