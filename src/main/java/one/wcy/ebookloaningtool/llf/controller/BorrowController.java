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
    @PostMapping("/borrow")
    public Response borrow(@RequestBody BooksRequest books) {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        List<String> bs = books.getBookId();
        return borrowService.recordBorrow(bs, userID);
    }

    @PostMapping("/return")
    public Response returnBook(@RequestBody Book book) {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.returnBook(b, userID);
    }

    @PostMapping("/renew")
    public Response renewBook(@RequestBody Book book) {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Book b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.renewBook(b, userID);
    }

    @PostMapping("/borrowlist")
    public Response borrowList() {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return borrowService.getBorrowList(userID);
    }

    @PostMapping("/history")
    public Response history() {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return borrowService.getBorrowHistory(userID);
    }
}
