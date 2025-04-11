package one.wcy.ebookloaningtool.llf.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "BorrowRecords")
@Data
public class BorrowRecords {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String borrowUUID;

    private String userUUID;
    private String bookUUID;
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private String status;
}


