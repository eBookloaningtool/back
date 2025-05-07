/**
 * Controller class for managing user's wishlist operations.
 * Handles adding, removing, and retrieving books from the user's wishlist.
 */
package one.wcy.ebookloaningtool.llf.controller;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.WishlistService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private WishlistService wishlistService;

    /**
     * Adds a book to the user's wishlist.
     * Verifies book existence before adding.
     * @param book The book to be added to the wishlist
     * @return Response indicating success or failure of the operation
     */
    @PostMapping("/add")
    public Response add(@RequestBody Book book){
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        if(b == null){
            //book not exist
            return new Response("Book not exist.");
        }
        else{
            //add book to wishlist
            return wishlistService.addBook(b.getBookId(), userID);
        }
    }

    /**
     * Removes a book from the user's wishlist.
     * @param book The book to be removed from the wishlist
     * @return Response indicating success or failure of the operation
     */
    @PostMapping("/delete")
    public Response delete(@RequestBody Book book){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return wishlistService.deleteBook(book.getBookId(), userID);
    }

    /**
     * Retrieves the current contents of the user's wishlist.
     * @return Response containing the list of books in the wishlist
     */
    @PostMapping("get")
    public Response get(){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return wishlistService.getWishlist(userID);
    }
}
