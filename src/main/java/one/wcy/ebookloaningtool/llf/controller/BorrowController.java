package one.wcy.ebookloaningtool.llf.controller;



import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.response.BorrowResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.security.JwtTokenService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/borrow")
    public Response borrow(@RequestHeader(name = "Authorization") String token, @RequestBody Books book) {
        //从令牌中获取用户uuid
        Claims claims = jwtTokenService.extractAllClaims(token);
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            //没找到对应书籍
            return new Response("Book not exist.");
        }
        else if (b.getStock() < 1) {
                //库存不足
                return new Response("Stock is too low.");
        }
        else{
                //借出书籍
                return borrowService.recordBorrow(b.getBookId(), userID);
        }
    }

    @PostMapping("/return")
    public Response returnBook(@RequestHeader(name = "Authorization") String token, @RequestBody Books book) {
        //从令牌中获取用户uuid
        Claims claims = jwtTokenService.extractAllClaims(token);
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.returnBook(b.getBookId(), userID);
    }

    @PostMapping("/renew")
    public Response renewBook(@RequestHeader(name = "Authorization") String token, @RequestBody Books book) {
        //从令牌中获取用户uuid
        Claims claims = jwtTokenService.extractAllClaims(token);
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        if (b == null) {
            return new Response("Book not exist.");
        }
        else return borrowService.renewBook(b.getBookId(), userID);
    }
}
