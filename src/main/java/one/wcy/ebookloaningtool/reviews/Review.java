package one.wcy.ebookloaningtool.reviews;

import jakarta.persistence.*;
import lombok.Data;
import one.wcy.ebookloaningtool.users.User;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "Comments")

public class Review {
    @Id
    @Column(name = "commentUUID", length = 255)
    // Primary key
    private String commentId;

    // User ID (Foreign key)
    @Column(name = "userUUID", nullable = false, length = 255)
    private String userUuid;

    // Book ID (Foreign key)
    @Column(name = "bookUUID", nullable = false, length = 255)
    private String bookUuid;

    // Comment content
    @Column(columnDefinition = "content", nullable = false)
    private String content;

    // Comment rating(1-5)
    @Column(columnDefinition = "rating",nullable = false)
    private Integer rating;

    // Creation time
    @Column(nullable = false)
    private LocalDateTime createdAt;

    // Update time
    private LocalDateTime updatedAt;

    // Logical deletion flag (0=Normal, 1=Deleted)
    private Integer deleted = 0;

    // Parent comment ID
    @Column(name = "parentCommentId", length = 255)
    private String parentCommentId;

    // Root comment ID
    @Column(name = "rootCommentId", length = 255)
    private String rootCommentId;

    // Association with User (Foreign key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userUUID", referencedColumnName = "userUUID", insertable = false, updatable = false)
    private User user;

    // Association with Book (Foreign key)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookUUID", referencedColumnName = "bookUUID", insertable = false, updatable = false)
    private Book book;

    // Child comments (for tree structure display)
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private List<Review> children = new ArrayList<>();
}
