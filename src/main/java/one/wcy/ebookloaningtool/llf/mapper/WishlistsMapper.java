/**
 * Mapper interface for Wishlists entity.
 * Provides database operations for managing user's wishlist,
 * including adding, removing, and retrieving wishlist items.
 */
package one.wcy.ebookloaningtool.llf.mapper;


import one.wcy.ebookloaningtool.llf.pojo.Wishlists;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WishlistsMapper {

    /**
     * Retrieves all book IDs in a user's wishlist.
     * @param id The unique identifier of the user
     * @return List of book IDs in the user's wishlist
     */
    @Select("select bookId from Wishlists where userUUID = #{id}")
    List<String> findListByUid(String id);

    /**
     * Retrieves a specific wishlist item by user ID and book ID.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book
     * @return The wishlist item if found, null otherwise
     */
    @Select("select * from Wishlists where userUUID = #{uid} and bookId = #{bid}")
    Wishlists findListByUidAndBookId(String uid, String bid);

    /**
     * Adds a book to the user's wishlist.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book to add
     */
    @Insert("insert into Wishlists(userUUID, bookId, addedAt) values(#{uid}, #{bid}, now())")
    void addWishlists(String uid, String bid);

    /**
     * Removes a book from the user's wishlist.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book to remove
     */
    @Delete("delete from Wishlists where userUUID = #{uid} and bookId = #{bid}")
    void deleteWishlists(String uid, String bid);

}
