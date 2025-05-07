/**
 * Mapper interface for Cart entity.
 * Provides database operations for managing user's shopping cart,
 * including adding, removing, and retrieving cart items.
 */
package one.wcy.ebookloaningtool.llf.mapper;


import one.wcy.ebookloaningtool.llf.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {

    /**
     * Retrieves all book IDs in a user's shopping cart.
     * @param id The unique identifier of the user
     * @return List of book IDs in the user's cart
     */
    @Select("select bookId from Cart where userUUID = #{id}")
    List<String> findListByUid(String id);

    /**
     * Retrieves a specific cart item by user ID and book ID.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book
     * @return The cart item if found, null otherwise
     */
    @Select("select * from Cart where userUUID = #{uid} and bookId = #{bid}")
    Cart findListByUidAndBookId(String uid, String bid);

    /**
     * Adds a book to the user's shopping cart.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book to add
     */
    @Insert("insert into Cart(userUUID, bookId, addedAt) values(#{uid}, #{bid}, now())")
    void addCart(String uid, String bid);

    /**
     * Removes a book from the user's shopping cart.
     * @param uid The unique identifier of the user
     * @param bid The unique identifier of the book to remove
     */
    @Delete("delete from Cart where userUUID = #{uid} and bookId = #{bid}")
    void deleteCart(String uid, String bid);

}
