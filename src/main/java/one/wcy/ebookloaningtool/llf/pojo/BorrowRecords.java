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
    @Column(name = "useruuid")
    private String userUUID;
    @Column(name = "bookuuid")
    private String bookUUID;
    @Column(name = "borrow_date")
    private LocalDateTime borrowDate;
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    @Column(name = "return_date")
    private LocalDateTime returnDate;
    @Column(name = "status")
    private String status;
}


