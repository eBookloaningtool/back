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

    @Autowired
    private WishlistsMapper wishlistMapper;
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    @Autowired
    private BorrowService borrowService;
    @Override
    public Response addBook(String bookId, String userID) {

        if (wishlistMapper.findListByUidAndBookId(userID, bookId) != null || borrowRecordsMapper.findByBookUUIDAndUserUUIDAndStatus(bookId,userID,"borrowed").size() > 0) {
            //书已在愿望清单中
            return new Response("Book already exist.");
        }
        else{
            //将书添加到愿望清单
            try {
                wishlistMapper.addWishlists(userID,bookId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new Response("success");
        }
    }

    @Override
    public Response deleteBook(String bookId, String userID) {
            Book b = borrowService.findBookById(bookId);
            //没有该书
            if(b == null) return new Response("The book does not exist.");
            if (wishlistMapper.findListByUidAndBookId(userID, bookId) == null)
                return new Response("The book does not exist in wishlist.");
        wishlistMapper.deleteWishlists(userID,bookId);
        return new Response("success");
    }

    @Override
    public Response getWishlist(String userID) {
        List<String> wishlist = wishlistMapper.findListByUid(userID);
        return new GetWishlistResponse("success", wishlist);
    }
}
