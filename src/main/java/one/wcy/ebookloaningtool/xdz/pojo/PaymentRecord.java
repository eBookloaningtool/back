package one.wcy.ebookloaningtool.xdz.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class representing a payment record in the database.
 * Contains information about a payment transaction including amount and timestamps.
 */
@Data
@Entity
@Table(name = "PaymentRecords")
public class PaymentRecord {
    /**
     * Unique identifier for the payment record
     */
    @Id
    @Column(name = "paymentId")
    private String paymentId;

    /**
     * Unique identifier of the user who made the payment
     */
    @Column(name = "uuid", nullable = false)
    private String uuid;

    /**
     * Amount of the payment
     */
    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    /**
     * Date and time when the payment was made
     */
    @Column(name = "paymentDate", nullable = false)
    private LocalDateTime paymentDate;
}
