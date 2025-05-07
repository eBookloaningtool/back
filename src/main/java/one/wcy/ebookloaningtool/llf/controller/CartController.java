/**
 * Controller class for managing user's shopping cart operations.
 * Handles adding, removing, and retrieving books from the user's cart.
 */
package one.wcy.ebookloaningtool.llf.controller;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.pojo.BooksRequest;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.CartService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private CartService cartService;

    /**
     * Adds a book to the user's shopping cart.
     * Checks book availability and stock before adding.
     * @param book The book to be added to the cart
     * @return Response indicating success or failure of the operation
     */
    @PostMapping("/add")
    public Response add(@RequestBody Book book){
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        //book not exist
        if(b == null) return new Response("Book not exist.");
        //no stock
        else if (b.getAvailableCopies() < 1) return new Response("Stock is too low.");
        //add book to wishlist
        else return cartService.addBook(b.getBookId(), userID);
    }

    /**
     * Removes one or more books from the user's shopping cart.
     * @param books Request containing list of book IDs to be removed
     * @return Response indicating success or failure of the operation
     */
    @PostMapping("/remove")
    public Response delete(@RequestBody BooksRequest books){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        List<String> bs = books.getBookId();
        return cartService.deleteBook(bs, userID);
    }

    /**
     * Retrieves the current contents of the user's shopping cart.
     * @return Response containing the list of books in the cart
     */
    @PostMapping("/get")
    public Response get(){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return cartService.getCart(userID);
    }
}
