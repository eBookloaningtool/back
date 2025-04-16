package one.wcy.ebookloaningtool.llf.controller;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.WishlistService;
import one.wcy.ebookloaningtool.utils.Response;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
public class wishlistController {

    @Autowired
    private BorrowService borrowService;
    @Autowired
    private WishlistService wishlistService;

    @PostMapping("/add")
    public Response add(@RequestBody Books book){
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        if(b == null){
            //没找到对应书籍
            return new Response("Book not exist.");
        }
        else{
            //将书添加到愿望清单
            return wishlistService.addBook(b.getBookId(), userID);
        }
    }

    @PostMapping("/delete")
    public Response delete(@RequestBody Books book){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        Books b = borrowService.findBookById(book.getBookId());
        if(b == null){
            //没有对应书籍
            return new Response("Book not exist.");
        }
        else{
            //从愿望清单中删除
            return wishlistService.deleteBook(b.getBookId(), userID);
        }
    }

    @PostMapping("get")
    public Response get(){
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        return wishlistService.getWishlist(userID);
    }
}
