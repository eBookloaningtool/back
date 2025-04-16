package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.WishlistsMapper;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.response.getWishlistResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.WishlistService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class wishlistServiceimpl implements WishlistService {

    @Autowired
    private WishlistsMapper wishlistMapper;
    @Autowired
    private BorrowService borrowService;
    @Override
    public Response addBook(String bookId, String userID) {
        if (wishlistMapper.findListByUidAndBookId(userID, bookId) != null) {
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
    public Response deleteBook(List<String> bookIds, String userID) {
        for (String bookId: bookIds){
            Books b = borrowService.findBookById(bookId);
            //没有该书
            if(b == null) return new Response("Some books not exist.");
            if (wishlistMapper.findListByUidAndBookId(userID, bookId) == null)
                return new Response("Some books does not exist in cart.");
        }
        for (String bookId: bookIds) wishlistMapper.deleteWishlists(userID,bookId);
        return new Response("success");
    }

    @Override
    public Response getWishlist(String userID) {
        List<String> wishlist = wishlistMapper.findListByUid(userID);
        return new getWishlistResponse("bookId", wishlist);
    }
}
