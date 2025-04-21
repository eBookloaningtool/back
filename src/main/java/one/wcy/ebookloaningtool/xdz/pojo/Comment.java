package one.wcy.ebookloaningtool.xdz.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Comments")
public class Comment {
    @Id
    @Column(name = "commentUUID")
    private String commentId; // 主键

    @Column(name = "userUUID", nullable = false)
    private String userUUID; // 外键关联Users表

    @Column(name = "bookUUID", nullable = false)
    private String bookUUID; // 外键关联Books表

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updateAt")
    private LocalDateTime updatedAt;

    @Column(name = "deleted", columnDefinition = "TINYINT DEFAULT 0")
    private Integer deleted; // 0=正常，1=已删除

}
