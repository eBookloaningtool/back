/**
 * Service interface for managing user's wishlist operations.
 * Provides methods for adding, removing, and retrieving books from the wishlist.
 */
package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

public interface WishlistService {
    /**
     * Adds a book to the user's wishlist.
     * @param bookId The ID of the book to be added
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    Response addBook(String bookId, String userID);

    /**
     * Removes a book from the user's wishlist.
     * @param bookId The ID of the book to be removed
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    Response deleteBook(String bookId, String userID);

    /**
     * Retrieves all books in the user's wishlist.
     * @param userID The ID of the user
     * @return Response containing the list of books in the wishlist
     */
    Response getWishlist(String userID);
}
