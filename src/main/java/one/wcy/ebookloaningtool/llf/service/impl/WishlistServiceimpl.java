/**
 * Implementation of the WishlistService interface.
 * Provides concrete implementation for managing user's wishlist operations.
 */
package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.BorrowRecordsMapper;
import one.wcy.ebookloaningtool.llf.mapper.WishlistsMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.response.GetWishlistResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.WishlistService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishlistServiceimpl implements WishlistService {

    /**
     * Mapper for database operations related to wishlists
     */
    @Autowired
    private WishlistsMapper wishlistMapper;

    /**
     * Mapper for database operations related to borrow records
     */
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    /**
     * Service for book borrowing operations
     */
    @Autowired
    private BorrowService borrowService;

    /**
     * Adds a book to the user's wishlist.
     * Checks if the book is already in the wishlist or currently borrowed.
     * @param bookId The ID of the book to be added
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response addBook(String bookId, String userID) {
        if (wishlistMapper.findListByUidAndBookId(userID, bookId) != null) {
            //the book is already in the wishlist
            return new Response("Book already exist.");
        } else if (!borrowRecordsMapper.findByBookUUIDAndUserUUIDAndStatus(bookId, userID, "borrowed").isEmpty()) {
            return new Response("Book already borrowed.");
        } else{
            //add the book to the wishlist
            try {
                wishlistMapper.addWishlists(userID,bookId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new Response("success");
        }
    }

    /**
     * Removes a book from the user's wishlist.
     * Verifies book existence and wishlist membership before removal.
     * @param bookId The ID of the book to be removed
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response deleteBook(String bookId, String userID) {
        Book b = borrowService.findBookById(bookId);
        //the book does not exist
        if(b == null) return new Response("The book does not exist.");
        if (wishlistMapper.findListByUidAndBookId(userID, bookId) == null)
            return new Response("The book does not exist in wishlist.");
        wishlistMapper.deleteWishlists(userID,bookId);
        return new Response("success");
    }

    /**
     * Retrieves all books in the user's wishlist.
     * @param userID The ID of the user
     * @return Response containing the list of books in the wishlist
     */
    @Override
    public Response getWishlist(String userID) {
        List<String> wishlist = wishlistMapper.findListByUid(userID);
        return new GetWishlistResponse("success", wishlist);
    }
}
