package one.wcy.ebookloaningtool.llf.pojo;


import lombok.Data;

import java.time.LocalDateTime;


@Data
public class Cart {
    private String userUUID;
    private String bookId;
    private LocalDateTime addedAt;
}
