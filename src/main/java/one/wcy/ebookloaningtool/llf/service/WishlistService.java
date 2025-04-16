package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

public interface WishlistService {
    Response addBook(String bookId, String userID);
    Response deleteBook(List<String> bookIds, String userID);
    Response getWishlist(String userID);
}
