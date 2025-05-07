/**
 * Service interface for managing book borrowing operations.
 * Provides methods for borrowing, returning, renewing books and managing borrowing records.
 */
package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

public interface BorrowService {

    /**
     * Finds a book by its UUID.
     * @param bookUUID The UUID of the book to find
     * @return The book object if found, null otherwise
     */
    Book findBookById(String bookUUID);

    /**
     * Records the borrowing of multiple books.
     * @param bookIds List of book IDs to be borrowed
     * @param userUUID The UUID of the user borrowing the books
     * @return Response indicating success or failure of the operation
     */
    Response recordBorrow(List<String> bookIds, String userUUID);

    /**
     * Processes the return of a borrowed book.
     * @param book The book to be returned
     * @param userUUID The UUID of the user returning the book
     * @return Response indicating success or failure of the operation
     */
    Response returnBook(Book book, String userUUID);

    /**
     * Processes the renewal of a borrowed book's loan period.
     * @param book The book to be renewed
     * @param userUUID The UUID of the user renewing the book
     * @return Response indicating success or failure of the operation
     */
    Response renewBook(Book book, String userUUID);

    /**
     * Sends reminders for overdue books.
     * @param i Parameter for reminder configuration
     */
    void overdueReminder(int i);

    /**
     * Automatically processes the return of overdue books.
     */
    void autoReturn();

    /**
     * Retrieves the content of an e-book.
     * @param bookUUID The UUID of the book
     * @param userUUID The UUID of the user requesting the content
     * @return The content of the book
     */
    String getBookContent(String bookUUID, String userUUID);

    /**
     * Retrieves the list of books currently borrowed by a user.
     * @param userID The ID of the user
     * @return Response containing the list of currently borrowed books
     */
    Response getBorrowList(String userID);

    /**
     * Retrieves the complete borrowing history of a user.
     * @param userID The ID of the user
     * @return Response containing the user's borrowing history
     */
    Response getBorrowHistory(String userID);
}
