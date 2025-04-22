package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.CartMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.response.getCartResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.llf.service.CartService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceimpl implements CartService {

    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private BorrowService borrowService;

    @Override
    public Response addBook(String bookId, String userID) {
        if (cartMapper.findListByUidAndBookId(userID, bookId) != null) {
            //书已在购物车中
            return new Response("Book already exist.");
        }
        else{
            //将书添加到购物车
            try {
                cartMapper.addCart(userID,bookId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return new Response("success");
        }
    }

    @Override
    public Response deleteBook(List<String> bookIds, String userID) {
        for (String bookId: bookIds){
            Book b = borrowService.findBookById(bookId);
            //没有该书
            if(b == null) return new Response("Some books not exist.");
            if (cartMapper.findListByUidAndBookId(userID, bookId) == null)
                return new Response("Some books does not exist in cart.");
        }
        for (String bookId: bookIds) cartMapper.deleteCart(userID,bookId);
        return new Response("success");
    }

    @Override
    public Response getCart(String userID) {
        List<String> cart = cartMapper.findListByUid(userID);
        return new getCartResponse("bookId", cart);
    }
}
