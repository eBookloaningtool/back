package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

public interface WishlistService {
    Response addBook(String bookId, String userID);
    Response deleteBook(String bookId, String userID);
    Response getWishlist(String userID);
}
