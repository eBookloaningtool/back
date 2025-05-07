/**
 * Service interface for managing user's shopping cart operations.
 * Provides methods for adding, removing, and retrieving books from the cart.
 */
package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

public interface CartService {
    /**
     * Adds a book to the user's shopping cart.
     * @param bookId The ID of the book to be added
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    Response addBook(String bookId, String userID);

    /**
     * Removes one or more books from the user's shopping cart.
     * @param bookIds List of book IDs to be removed
     * @param userID The ID of the user
     * @return Response indicating success or failure of the operation
     */
    Response deleteBook(List<String> bookIds, String userID);

    /**
     * Retrieves all books in the user's shopping cart.
     * @param userID The ID of the user
     * @return Response containing the list of books in the cart
     */
    Response getCart(String userID);
}
