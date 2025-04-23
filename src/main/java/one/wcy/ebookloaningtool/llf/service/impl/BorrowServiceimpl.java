package one.wcy.ebookloaningtool.llf.service.impl;

import lombok.extern.slf4j.Slf4j;
import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.mapper.BorrowRecordsMapper;
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
import java.util.List;

@Service
@Slf4j
public class BorrowServiceimpl implements BorrowService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BorrowRecordsMapper borrowRecordsMapper;
    @Autowired
    private EmailService emailService;

    //一次租借时长
    private final int BORROW_DURATION = 30;
    //续借时长
    private final int RENEW_DURATION = 30;

    public BorrowServiceimpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Book findBookById(String bookUUID){
        Book b = bookMapper.findBookById(bookUUID);
        return b;
    }

    //记录借出
    @Override
    public Response recordBorrow(Book book, String userUUID) {

        String bookUUID = book.getBookId();
        BigDecimal price = book.getPrice();

        if(borrowRecordsMapper.countBorrow(userUUID,"borrowed")>=10){
            //同时借阅数量已达上限
            return new Response("Reach borrow limit");
        }
        BigDecimal balance = userRepository.findByUuid(userUUID).getBalance();
        BigDecimal newBalance = balance.subtract(price);//计算扣款
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            //余额不足
            return new LowBalanceResponse("insufficient balance", newBalance.negate());//返回需要补齐的金额
        }
        //判断是否已经有该书的借出记录
        if(checkBorrow(bookUUID, userUUID).isEmpty()){
            //书本库存-1
            //书本借阅次数+1
            book.setBorrowTimes(book.getBorrowTimes() + 1);
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookMapper.updateBook(book);
            //在BorrowRecords中记录借出
            LocalDate dueTime;
            LocalDate borrowTime = LocalDate.now();
            dueTime = borrowTime.plusDays(BORROW_DURATION);
            BorrowRecords borrowRecords = new BorrowRecords();
            borrowRecords.setBookId(bookUUID);
            borrowRecords.setUuid(userUUID);
            borrowRecords.setBorrowDate(borrowTime);
            borrowRecords.setDueDate(dueTime);
            borrowRecords.setStatus("borrowed");
            borrowRecordsMapper.addBorrowRecord(borrowRecords);
            //更新当前用户的账户余额
            User user = userRepository.findByUuid(userUUID);
            user.setBalance(newBalance);
            userRepository.save(user);
            // 发送借阅成功邮件
            String subject = "eBook borrow system - Borrow notification";
            String emailBody = buildBorrowEmailBody(user.getName(), book.getTitle(), price, "borrow");
            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
            if (!emailSent) {
                log.error("Failed to send email to: {} ", user.getEmail());
            }
            log.info("Email sent to: {} ", user.getEmail());
            return new BorrowResponse("Borrow Successful", dueTime, newBalance);
        }
        else return new Response("The user already borrowed this book.");

    }

    @Override
    public Response returnBook(Book book, String userUUID) {

        String bookUUID = book.getBookId();
        //判断是否存在正在借出记录
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //如果存在则将该书库存+1，将对应记录的status改为"returned"，并记录归还日期,否则返回当前未借阅该书
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookMapper.updateBook(book);
            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDate returnTime = LocalDate.now();
            borrowedRecord.setReturnDate(returnTime);
            borrowedRecord.setStatus("returned");
            borrowRecordsMapper.updateBorrowRecord(borrowedRecord);
            User user = userRepository.findByUuid(userUUID);
            // 发送还书成功邮件
            String subject = "eBook borrow system - Return notification";
            String emailBody = buildBorrowEmailBody(user.getName(), book.getTitle(), book.getPrice(), "return");
            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
            if (!emailSent) {
                log.error("Failed to send email to: {} ", user.getEmail());
            }
            log.info("Email sent to: {} ", user.getEmail());
            return new Response("Return Successful");
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public Response renewBook(Book book, String userUUID) {
        String bookUUID = book.getBookId();
        BigDecimal price = book.getPrice();
        BigDecimal balance = userRepository.findByUuid(userUUID).getBalance();
        BigDecimal newBalance = balance.subtract(price);//计算扣款
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            //余额不足
            return new LowBalanceResponse("insufficient balance", newBalance.negate());//返回需要补齐的金额
        }
        //判断是否存在正在借出记录
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //如果存在则延长逾期日期，否则返回当前未借阅该书
            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDate newDueTime = borrowedRecord.getDueDate().plusDays(RENEW_DURATION);
            borrowedRecord.setDueDate(newDueTime);
            borrowRecordsMapper.updateBorrowRecord(borrowedRecord);
            //书借阅次数+1
            book.setBorrowTimes(book.getBorrowTimes() + 1);
            bookMapper.updateBook(book);
            //用户余额更新
            User user = userRepository.findByUuid(userUUID);
            user.setBalance(newBalance);
            userRepository.save(user);

            // 发送续借成功邮件
            String subject = "eBook borrow system - Re-borrow notification";
            String emailBody = buildBorrowEmailBody(user.getName(), book.getTitle(), price, "re-borrow");
            boolean emailSent = emailService.sendHtmlEmail(user.getEmail(), subject, emailBody);
            if (!emailSent) {
                log.error("Failed to send email to: {} ", user.getEmail());
            }
            log.info("Email sent to: {} ", user.getEmail());
            return new RenewResponse("Renew Successful",newDueTime, newBalance);
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public void overdueReminder(int i) {
        List<BorrowRecords> brl = borrowRecordsMapper.findByStatus("borrowed");
        LocalDate ReminderDate = LocalDate.now().plusDays(i);//根据传入i，计算需要提醒的书籍的逾期日期

        for (BorrowRecords borrowRecords : brl) {
            LocalDate dueDate = borrowRecords.getDueDate();//获取逾期日期
            if (ReminderDate.equals(dueDate)) {
                String userUUID = borrowRecords.getUuid();
                User user = userRepository.findByUuid(userUUID);
                Book book = bookMapper.findBookById(borrowRecords.getBookId());
                // 发送逾期提醒邮件
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

    @Override
    public void autoReturn() {
        LocalDate dueDate = LocalDate.now();
        List<BorrowRecords> brl = borrowRecordsMapper.findByStatus("borrowed");
        for (BorrowRecords borrowRecords : brl) {
            if (borrowRecords.getDueDate().equals(dueDate)) {
                //更改借阅状态
                borrowRecords.setStatus("returned");
                //设置归还时间为当前时间
                borrowRecords.setReturnDate(LocalDate.now());
                borrowRecordsMapper.updateBorrowRecord(borrowRecords);
                //库存+1
                Book book = bookMapper.findBookById(borrowRecords.getBookId());
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookMapper.updateBook(book);

                User user = userRepository.findByUuid(borrowRecords.getUuid());


                // 发送自动还书邮件
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

    @Override
    //获取借阅列表
    public Response getBorrowList(String userID) {
        List<BorrowList> brl = borrowRecordsMapper.findBorrowList(userID, "borrowed");
        return new BorrowListResponse("success", brl);
    }

    @Override
    //获取借阅历史记录
    public Response getBorrowHistory(String userID) {
        List<BorrowHistory> brl = borrowRecordsMapper.findBorrowHistory(userID);
        return new BorrowHistoryResponse("success", brl);
    }

    private List<BorrowRecords> checkBorrow(String bookUUID, String userUUID) {
        return borrowRecordsMapper.findByBookUUIDAndUserUUIDAndStatus(bookUUID, userUUID, "borrowed");
    }

    @Override
    public String getBookContent(String bookUUID, String userUUID) {
        // 检查用户是否正在借阅该书
        List<BorrowRecords> borrowRecords = checkBorrow(bookUUID, userUUID);
        if (borrowRecords.isEmpty()) {
            return null; // 用户未借阅该书，返回null
        }
        
        // 用户正在借阅该书，返回内容URL
        Book book = bookMapper.findBookById(bookUUID);
        return book != null ? book.getContentURL() : null;
    }

    /**
     * 构建借阅邮件正文
     */
    private String buildBorrowEmailBody(String userName, String bookName, BigDecimal price, String function) {
        return "<html><body>" +
                "<h2>ebookloaningtool</h2>" +
                "<p>Dear " + userName + "：</p>" +
                "<p>Congratulations!</p>" +
                "<p>You successfully "+ function + ": </p>" +
                "<p>    Book: " + bookName + "</p>" +
                "<p>    Price: " + price + "</p>" +
                "<p>If you have a question, contact us.</p>" +
                "<p>Thank you！</p>" +
                "</body></html>";
    }

    /**
     * 构建自动归还邮件正文
     */
    private String buildAutoReturnEmailBody(String userName, String bookName, String function, LocalDate returnDate) {
        return "<html><body>" +
                "<h2>ebookloaningtool</h2>" +
                "<p>Dear " + userName + "：</p>" +
                "<p>Notice</p>" +
                "<p>Your borrowed book: " + bookName + "</p>" +
                "<p>" + function + returnDate +"</p>" +
                "<p>If you have a question, contact us.</p>" +
                "<p>Thank you！</p>" +
                "</body></html>";
    }
}
