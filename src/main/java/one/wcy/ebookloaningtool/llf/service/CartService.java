package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

public interface CartService {
    Response addBook(String bookId, String userID);
    Response deleteBook(List<String> bookIds, String userID);
    Response getCart(String userID);
}
