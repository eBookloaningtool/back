/**
 * Implementation of the CartService interface.
 * Provides concrete implementation for managing user's shopping cart operations.
 */
package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.BorrowRecordsMapper;
import one.wcy.ebookloaningtool.llf.mapper.CartMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.response.GetCartResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.CartService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceimpl implements CartService {

    /**
     * Mapper for database operations related to shopping cart
     */
    @Autowired
    private CartMapper cartMapper;

    /**
     * Service for book borrowing operations
     */
    @Autowired
    private BorrowService borrowService;

    /**
     * Mapper for database operations related to borrow records
     */
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    /**
     * Adds a book to the user's shopping cart.
     * Checks if the book is already in the cart or currently borrowed.
     * @param bookId The ID of the book to be added
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response addBook(String bookId, String userID) {
        if (cartMapper.findListByUidAndBookId(userID, bookId) != null) {
            //the book is already in the cart
            return new Response("Book already exist.");
        }
        else if (!borrowRecordsMapper.findByBookUUIDAndUserUUIDAndStatus(bookId, userID, "borrowed").isEmpty()) {
            return new Response("Book already borrowed.");
        }
        else{
            //add the book to the cart
            try {
                cartMapper.addCart(userID,bookId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new Response("success");
        }
    }

    /**
     * Removes one or more books from the user's shopping cart.
     * Verifies book existence and cart membership before removal.
     * @param bookIds List of book IDs to be removed
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response deleteBook(List<String> bookIds, String userID) {
        for (String bookId: bookIds){
            Book b = borrowService.findBookById(bookId);
            //the book does not exist
            if(b == null) return new Response("Some books not exist.");
            if (cartMapper.findListByUidAndBookId(userID, bookId) == null)
                return new Response("Some books does not exist in cart.");
        }
        for (String bookId: bookIds) cartMapper.deleteCart(userID,bookId);
        return new Response("success");
    }

    /**
     * Retrieves all books in the user's shopping cart.
     * @param userID The ID of the user
     * @return Response containing the list of books in the cart
     */
    @Override
    public Response getCart(String userID) {
        List<String> cart = cartMapper.findListByUid(userID);
        return new GetCartResponse("success", cart);
    }
}
