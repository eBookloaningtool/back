/**
 * Implementation of the BorrowService interface.
 * Provides concrete implementation for managing book borrowing operations,
 * including borrowing, returning, renewing books, and handling overdue notifications.
 */
package one.wcy.ebookloaningtool.llf.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.mapper.BorrowRecordsMapper;
import one.wcy.ebookloaningtool.llf.mapper.CartMapper;
import one.wcy.ebookloaningtool.llf.mapper.WishlistsMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.pojo.BorrowHistory;
import one.wcy.ebookloaningtool.llf.pojo.BorrowList;
import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import one.wcy.ebookloaningtool.llf.response.*;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.notification.EmailService;
import one.wcy.ebookloaningtool.users.User;
import one.wcy.ebookloaningtool.users.UserRepository;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class BorrowServiceimpl implements BorrowService {

    /**
     * Repository for user data access
     */
    @Autowired
    private final UserRepository userRepository;

    /**
     * Mapper for database operations related to books
     */
    @Autowired
    private BookMapper bookMapper;

    /**
     * Mapper for database operations related to borrow records
     */
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;

    /**
     * Mapper for database operations related to wishlists
     */
    @Autowired
    private WishlistsMapper wishlistsMapper;

    /**
     * Mapper for database operations related to shopping cart
     */
    @Autowired
    private CartMapper cartMapper;

    /**
     * Service for sending email notifications
     */
    @Autowired
    private EmailService emailService;

    /**
     * Duration of a single borrowing period in days
     */
    private final int BORROW_DURATION = 30;

    /**
     * Duration of a renewal period in days
     */
    private final int RENEW_DURATION = 30;

    /**
     * Constructor for BorrowServiceimpl with dependency injection.
     * @param userRepository Repository for user data access
     */
    public BorrowServiceimpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Finds a book by its UUID.
     * @param bookUUID The UUID of the book to find
     * @return The book object if found, null otherwise
     */
    @Override
    public Book findBookById(String bookUUID){
        Book b = bookMapper.findBookById(bookUUID);
        return b;
    }

    /**
     * Records the borrowing of multiple books.
     * Handles validation, payment, and notification processes.
     * @param bookIds List of book IDs to be borrowed
     * @param userUUID The UUID of the user borrowing the books
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response recordBorrow(List<String> bookIds, String userUUID) {
        BigDecimal price = BigDecimal.ZERO;
        List<String> invalidBookIds = new ArrayList<>(); //record invalid book IDs
        List<String> lowStockBookIds = new ArrayList<>(); //record low stock book IDs
        List<String> borrowedBookIds = new ArrayList<>(); //record borrowed book IDs
        List<String> bookNames = new ArrayList<>();//record book names
        //calculate total price, and confirm book is valid
        for(String bId : bookIds){
            Book book = bookMapper.findBookById(bId);
            if(book == null){
                //book not exist
                invalidBookIds.add(bId);
            }
            else if (book.getAvailableCopies() < 1){
                //low stock
                lowStockBookIds.add(bId);
            }
            else if (!checkBorrow(bId,userUUID).isEmpty()) {
                //already borrowed
                borrowedBookIds.add(bId);
            } else{
                price = price.add(book.getPrice());
            }
        }
        //if there are invalid/low stock/already borrowed books, return borrow failed information
        if (!invalidBookIds.isEmpty() || !lowStockBookIds.isEmpty() || !borrowedBookIds.isEmpty()) {
            return new BorrowFailedResponse("Borrow failed.", invalidBookIds, lowStockBookIds, borrowedBookIds);
        }

        //borrow limit reached
        if(borrowRecordsMapper.countBorrow(userUUID,"borrowed") + bookIds.size() >10){
            return new Response("Reach borrow limit");
        }

        BigDecimal balance = userRepository.findByUuid(userUUID).getBalance();
        BigDecimal newBalance = balance.subtract(price);//calculate the deduction
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            //insufficient balance
            return new LowBalanceResponse("insufficient balance", newBalance.negate());//return the amount needed to be paid
        }

        LocalDate dueTime;
        LocalDate borrowTime = LocalDate.now();
        dueTime = borrowTime.plusDays(BORROW_DURATION);

        for(String bId : bookIds){
            Book book = bookMapper.findBookById(bId);
            bookNames.add(book.getTitle());//record book names
            //book stock -1
            //book borrow times +1
            book.setBorrowTimes(book.getBorrowTimes() + 1);
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookMapper.updateBook(book);
            //if the book is in the wishlist, remove it from the wishlist
            wishlistsMapper.deleteWishlists(userUUID, bId);
            //remove from cart
            cartMapper.deleteCart(userUUID, bId);
            //record borrow in BorrowRecords
            BorrowRecords borrowRecords = new BorrowRecords();
            borrowRecords.setBookId(bId);
            borrowRecords.setUuid(userUUID);
            borrowRecords.setBorrowDate(borrowTime);
            borrowRecords.setDueDate(dueTime);
            borrowRecords.setStatus("borrowed");
            borrowRecordsMapper.addBorrowRecord(borrowRecords);
        }
            //update user's balance
            User user = userRepository.findByUuid(userUUID);
            user.setBalance(newBalance);
            userRepository.save(user);
            //send borrow success email
        //send email asynchronously
        CompletableFuture.runAsync(() -> {
            try {
                String subject = "eBook borrow system - Borrow notification";
                String emailBody = buildBorrowEmailBody(user.getName(), bookNames, "borrow");
                boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
                if (!emailSent) {
                    log.error("Failed to send email to: {} ", user.getEmail());
                }
                log.info("Email sent to: {} ", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send email to: {} ", user.getEmail(), e);
            }
        });
//            String subject = "eBook borrow system - Borrow notification";
//            String emailBody = buildBorrowEmailBody(user.getName(), bookNames, "borrow");
//            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
//            if (!emailSent) {
//                log.error("Failed to send email to: {} ", user.getEmail());
//            }
//            log.info("Email sent to: {} ", user.getEmail());
            return new BorrowResponse("success", dueTime, newBalance);
    }

    /**
     * Processes the return of a borrowed book.
     * Updates book availability and sends return notification.
     * @param book The book to be returned
     * @param userUUID The UUID of the user returning the book
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response returnBook(Book book, String userUUID) {

        String bookUUID = book.getBookId();
        //check if there is a borrowing record
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //if there is a borrowing record, add 1 to the book stock, change the status to "returned", and record the return date, otherwise return the book is not borrowed
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookMapper.updateBook(book);
            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDate returnTime = LocalDate.now();
            borrowedRecord.setReturnDate(returnTime);
            borrowedRecord.setStatus("returned");
            borrowRecordsMapper.updateBorrowRecord(borrowedRecord);
            User user = userRepository.findByUuid(userUUID);
            List<String> bookNames = new ArrayList<>();
            bookNames.add(book.getTitle());
            //send email asynchronously
            CompletableFuture.runAsync(() -> {
                        try {
                            String subject = "eBook borrow system - Return notification";
                            String emailBody = buildBorrowEmailBody(user.getName(), bookNames, "return");
                            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
                            if (!emailSent) {
                                log.error("Failed to send email to: {} ", user.getEmail());
                            }
                            log.info("Email sent to: {} ", user.getEmail());
                        } catch (Exception e) {
                            log.error("Failed to send email to: {} ", user.getEmail(), e);
                        }
                    });
            return new Response("success");
        }else return new Response("The user do not borrow this book.");
    }

    /**
     * Processes the renewal of a borrowed book's loan period.
     * Handles payment and extends the due date.
     * @param book The book to be renewed
     * @param userUUID The UUID of the user renewing the book
     * @return Response indicating success or failure of the operation
     */
    @Override
    public Response renewBook(Book book, String userUUID) {
        String bookUUID = book.getBookId();
        BigDecimal price = book.getPrice();
        BigDecimal balance = userRepository.findByUuid(userUUID).getBalance();
        BigDecimal newBalance = balance.subtract(price);//calculate the deduction
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            //insufficient balance
            return new LowBalanceResponse("insufficient balance", newBalance.negate());//return the amount needed to be paid
        }
        //check if there is a borrowing record
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //if there is a borrowing record, extend the overdue date, otherwise return the book is not borrowed
            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDate newDueTime = borrowedRecord.getDueDate().plusDays(RENEW_DURATION);
            borrowedRecord.setDueDate(newDueTime);
            borrowRecordsMapper.updateBorrowRecord(borrowedRecord);
            //book borrow times +1
            book.setBorrowTimes(book.getBorrowTimes() + 1);
            bookMapper.updateBook(book);
            //user balance update
            User user = userRepository.findByUuid(userUUID);
            user.setBalance(newBalance);
            userRepository.save(user);
            List<String> bookNames = new ArrayList<>();
            bookNames.add(book.getTitle());
            //send renew success email
            String subject = "eBook borrow system - Re-borrow notification";
            String emailBody = buildBorrowEmailBody(user.getName(), bookNames, "re-borrow");
            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
            if (!emailSent) {
                log.error("Failed to send email to: {} ", user.getEmail());
            }
            log.info("Email sent to: {} ", user.getEmail());
            return new RenewResponse("success",newDueTime, newBalance);
        }else return new Response("The user do not borrow this book.");
    }

    /**
     * Sends reminders for books that will be due soon.
     * @param i Number of days before due date to send reminder
     */
    @Override
    public void overdueReminder(int i) {
        List<BorrowRecords> brl = borrowRecordsMapper.findByStatus("borrowed");
        LocalDate ReminderDate = LocalDate.now().plusDays(i);//calculate the overdue date of the books that need to be reminded

        for (BorrowRecords borrowRecords : brl) {
            LocalDate dueDate = borrowRecords.getDueDate();//get overdue date
            if (ReminderDate.equals(dueDate)) {
                String userUUID = borrowRecords.getUuid();
                User user = userRepository.findByUuid(userUUID);
                Book book = bookMapper.findBookById(borrowRecords.getBookId());
                //send overdue reminder email
                String subject = "eBook borrow system - Overdue reminder";
                String emailBody = buildAutoReturnEmailBody(user.getName(), book.getTitle(), "will reach its due date on ", borrowRecords.getDueDate());
                boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
                if (!emailSent) {
                    log.error("Failed to send email to: {} ", user.getEmail());
                }
                log.info("Email sent to: {} ", user.getEmail());
            }
        }
    }

    /**
     * Automatically processes the return of overdue books.
     * Updates book status and sends notifications.
     */
    @Override
    public void autoReturn() {
        LocalDate dueDate = LocalDate.now();
        List<BorrowRecords> brl = borrowRecordsMapper.findByStatus("borrowed");
        for (BorrowRecords borrowRecords : brl) {
            if (borrowRecords.getDueDate().equals(dueDate)) {
                //change borrow status
                borrowRecords.setStatus("returned");
                //set return date to current time
                borrowRecords.setReturnDate(LocalDate.now());
                borrowRecordsMapper.updateBorrowRecord(borrowRecords);
                //book stock +1
                Book book = bookMapper.findBookById(borrowRecords.getBookId());
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookMapper.updateBook(book);

                User user = userRepository.findByUuid(borrowRecords.getUuid());


                //send auto return email
                String subject = "eBook borrow system - Auto-return notification";
                String emailBody = buildAutoReturnEmailBody(user.getName(), book.getTitle(), "reach its due date on ", borrowRecords.getDueDate());
                boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
                if (!emailSent) {
                    log.error("Failed to send email to: {} ", user.getEmail());
                }
                log.info("Email sent to: {} ", user.getEmail());
            }
        }
    }

    /**
     * Retrieves the list of books currently borrowed by a user.
     * @param userID The ID of the user
     * @return Response containing the list of currently borrowed books
     */
    @Override
    public Response getBorrowList(String userID) {
        List<BorrowList> brl = borrowRecordsMapper.findBorrowList(userID, "borrowed");
        return new BorrowListResponse("success", brl);
    }

    /**
     * Retrieves the complete borrowing history of a user.
     * @param userID The ID of the user
     * @return Response containing the user's borrowing history
     */
    @Override
    public Response getBorrowHistory(String userID) {
        List<BorrowHistory> brl = borrowRecordsMapper.findBorrowHistory(userID);
        return new BorrowHistoryResponse("success", brl);
    }

    /**
     * Checks if a book is currently borrowed by a user.
     * @param bookUUID The UUID of the book to check
     * @param userUUID The UUID of the user
     * @return List of borrow records for the book and user
     */
    private List<BorrowRecords> checkBorrow(String bookUUID, String userUUID) {
        return borrowRecordsMapper.findByBookUUIDAndUserUUIDAndStatus(bookUUID, userUUID, "borrowed");
    }

    /**
     * Retrieves the content of an e-book.
     * @param bookUUID The UUID of the book
     * @param userUUID The UUID of the user requesting the content
     * @return The content of the book
     */
    @Override
    public String getBookContent(String bookUUID, String userUUID) {
        //check if the user is borrowing the book
        List<BorrowRecords> borrowRecords = checkBorrow(bookUUID, userUUID);
        if (borrowRecords.isEmpty()) {
            return null; //the user is not borrowing the book, return null
        }
        
        //the user is borrowing the book, return the content URL
        Book book = bookMapper.findBookById(bookUUID);
        return book != null ? book.getContentURL() : null;
    }

    /**
     * Builds the HTML email body for borrow-related notifications.
     * @param userName Name of the user
     * @param bookNames List of book names
     * @param function Type of operation (borrow/return/re-borrow)
     * @return HTML formatted email body
     */
    private String buildBorrowEmailBody(String userName, List<String> bookNames, String function) {
        StringBuilder htmlBody = new StringBuilder("<html><body>");
        htmlBody.append("<h2>BorrowBee</h2>");
        htmlBody.append("<p>Dear ").append(userName).append(":</p>");
        htmlBody.append("<p>Congratulations!</p>");
        htmlBody.append("<p>You successfully ").append(function).append(" the following books:</p>");

        for (String bookName : bookNames) {
            htmlBody.append("<p><i>  ").append(bookName).append("</i></p>");
        }
        htmlBody.append("<p>If you have a question, contact us.</p>");
        htmlBody.append("<p>Thank you! </p>");
        htmlBody.append("</body></html>");
        return htmlBody.toString();
    }

    /**
     * Builds the HTML email body for auto-return notifications.
     * @param userName Name of the user
     * @param bookName Name of the book
     * @param function Type of notification
     * @param returnDate Date of return
     * @return HTML formatted email body
     */
    private String buildAutoReturnEmailBody(String userName, String bookName, String function, LocalDate returnDate) {
        return "<html><body>" +
                "<h2>BorrowBee</h2>" +
                "<p>Dear " + userName + "：</p>" +
                "<p>Notice</p>" +
                "<p>Your borrowed book: " + bookName + "</p>" +
                "<p>" + function + returnDate +"</p>" +
                "<p>If you have a question, contact us.</p>" +
                "<p>Thank you！</p>" +
                "</body></html>";
    }
}
