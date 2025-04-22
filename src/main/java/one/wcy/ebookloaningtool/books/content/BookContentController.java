package one.wcy.ebookloaningtool.books.content;

import io.jsonwebtoken.Claims;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookContentController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/content")
    public ResponseEntity<BookContentResponse> getBookContent(@RequestBody BookContentRequest request) {
        //从令牌中获取用户uuid
        Claims claims = ThreadLocalUtil.get();
        String userID = claims.get("uuid").toString();
        
        String contentURL = borrowService.getBookContent(request.getBookId(), userID);
        if (contentURL == null) {
            return ResponseEntity.badRequest().body(new BookContentResponse(request.getBookId(), "You have not borrowed this book."));
        }
        
        return ResponseEntity.ok(new BookContentResponse(request.getBookId(), contentURL));
    }
}
