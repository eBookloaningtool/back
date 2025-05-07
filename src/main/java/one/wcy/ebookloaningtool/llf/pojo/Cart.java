/**
 * Entity class representing an item in a user's shopping cart.
 * Tracks books that users have added to their cart for potential borrowing.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Cart {
    /**
     * Unique identifier of the user who owns the cart
     */
    private String userUUID;

    /**
     * ID of the book added to the cart
     */
    private String bookId;

    /**
     * Timestamp when the book was added to the cart
     */
    private LocalDateTime addedAt;
}
