package one.wcy.ebookloaningtool.xdz.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @Column(name = "commentId")
    private String commentId; // 主键

    @Column(name = "uuid", nullable = false)
    private String uuid; // 外键关联Users表

    @Column(name = "bookId", nullable = false)
    private String bookId; // 外键关联Books表

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updateAt")
    private LocalDateTime updatedAt;

    @Column(name = "rating", nullable = false)
    private BigDecimal rating;

}
