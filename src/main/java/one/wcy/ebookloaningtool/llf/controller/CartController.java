package one.wcy.ebookloaningtool.llf.controller;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.pojo.CartRemoveRequest;
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

    @PostMapping("/add")
    public Response add(@RequestBody Books book){
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        //没找到对应书籍
        if(b == null) return new Response("Book not exist.");
        //没有库存
        else if (b.getStock() < 1) return new Response("Stock is too low.");
        //将书添加到愿望清单
        else return cartService.addBook(b.getBookId(), userID);
    }

    @PostMapping("/remove")
    public Response delete(@RequestBody CartRemoveRequest books){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        List<String> bs = books.getBookId();
        return cartService.deleteBook(bs, userID);
    }

    @PostMapping("/get")
    public Response get(){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return cartService.getCart(userID);
    }
}
