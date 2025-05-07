/**
 * Entity class representing an item in a user's wishlist.
 * Tracks books that users have marked as desired for future borrowing.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Wishlists {
    /**
     * Unique identifier of the user who owns the wishlist
     */
    private String userUUID;

    /**
     * ID of the book added to the wishlist
     */
    private String bookId;

    /**
     * Timestamp when the book was added to the wishlist
     */
    private LocalDateTime addedAt;
}
