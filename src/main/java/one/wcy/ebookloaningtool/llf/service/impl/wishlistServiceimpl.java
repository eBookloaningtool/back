package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.WishlistsMapper;
import one.wcy.ebookloaningtool.llf.response.getWishlistResponse;
import one.wcy.ebookloaningtool.llf.service.WishlistService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class wishlistServiceimpl implements WishlistService {

    @Autowired
    private WishlistsMapper wishlistMapper;
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
    public Response deleteBook(String bookId, String userID) {
        if (wishlistMapper.findListByUidAndBookId(userID, bookId) == null) {
            return new Response("Book does not exist in wishlist.");
        }
        else{
            wishlistMapper.deleteWishlists(userID,bookId);
            return new Response("success");
        }
    }

    @Override
    public Response getWishlist(String userID) {
        List<String> wishlist = wishlistMapper.findListByUid(userID);
        return new getWishlistResponse("bookId", wishlist);
    }
}
