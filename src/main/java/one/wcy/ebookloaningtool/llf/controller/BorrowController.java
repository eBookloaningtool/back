/**
 * Controller class for managing book borrowing operations.
 * Handles borrowing, returning, renewing books and viewing borrowing history.
 */
package one.wcy.ebookloaningtool.llf.controller;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.pojo.BooksRequest;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    /**
     * Handles the borrowing of multiple books.
     * @param books Request containing list of book IDs to be borrowed
     * @return Response indicating success or failure of the borrowing operation
     */
    @PostMapping("/borrow")
    public Response borrow(@RequestBody BooksRequest books) {
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        List<String> bs = books.getBookId();
        return borrowService.recordBorrow(bs, userID);
    }

    /**
     * Handles the return of a borrowed book.
     * @param book The book to be returned
     * @return Response indicating success or failure of the return operation
     */
    @PostMapping("/return")
    public Response returnBook(@RequestBody Book book) {
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.returnBook(b, userID);
    }

    /**
     * Handles the renewal of a borrowed book's loan period.
     * @param book The book to be renewed
     * @return Response indicating success or failure of the renewal operation
     */
    @PostMapping("/renew")
    public Response renewBook(@RequestBody Book book) {
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.renewBook(b, userID);
    }

    /**
     * Retrieves the list of books currently borrowed by the user.
     * @return Response containing the list of currently borrowed books
     */
    @PostMapping("/borrowlist")
    public Response borrowList() {
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return borrowService.getBorrowList(userID);
    }

    /**
     * Retrieves the complete borrowing history of the user.
     * @return Response containing the user's borrowing history
     */
    @PostMapping("/history")
    public Response history() {
        //get userID from token
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return borrowService.getBorrowHistory(userID);
    }
}
