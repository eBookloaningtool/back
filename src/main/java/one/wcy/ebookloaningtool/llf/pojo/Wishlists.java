package one.wcy.ebookloaningtool.llf.pojo;


import lombok.Data;
import java.time.LocalDateTime;


@Data
public class Wishlists {
    private String userUUID;
    private String bookId;
    private LocalDateTime addedAt;
}
