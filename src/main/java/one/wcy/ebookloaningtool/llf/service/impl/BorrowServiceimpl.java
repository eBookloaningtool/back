package one.wcy.ebookloaningtool.llf.service.impl;

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
    public Response recordBorrow(String bookUUID, String userUUID, BigDecimal price) {
        BigDecimal balance = userRepository.findByUuid(userUUID).getBalance();
        BigDecimal newBalance = balance.subtract(price);//计算扣款
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            //余额不足
            return new LowBalanceResponse("insufficient balance", newBalance.negate());//返回需要补齐的金额
        }
        //判断是否已经有该书的借出记录
        if(checkBorrow(bookUUID, userUUID).isEmpty()){
            //书本库存-1
            bookMapper.updateAvailableCopies(bookUUID, -1);
            //书本借阅次数+1
            bookMapper.updateBorrowTimes(bookUUID);
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
            return new BorrowResponse("Borrow Successful", dueTime, newBalance);
        }
        else return new Response("The user already borrowed this book.");

    }

    @Override
    public Response returnBook(String bookUUID, String userUUID) {

        //判断是否存在正在借出记录
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //如果存在则将该书库存+1，将对应记录的status改为"returned"，并记录归还日期,否则返回当前未借阅该书
            bookMapper.updateAvailableCopies(bookUUID, 1);

            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDate returnTime = LocalDate.now();
            borrowedRecord.setReturnDate(returnTime);
            borrowedRecord.setStatus("returned");
            borrowRecordsMapper.updateBorrowRecord(borrowedRecord);
            return new Response("Return Successful");
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public Response renewBook(String bookUUID, String userUUID, BigDecimal price) {
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
            bookMapper.updateBorrowTimes(bookUUID);
            //用户余额更新
            User user = userRepository.findByUuid(userUUID);
            user.setBalance(newBalance);
            userRepository.save(user);
            return new RenewResponse("Renew Successful",newDueTime, newBalance);
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public void overdueReminder(int i) {
        //List<BorrowRecords> brl = borrowRecordsRepository.findByStatus("borrowed");
        List<BorrowRecords> brl = borrowRecordsMapper.findByStatus("borrowed");
        LocalDate ReminderDate = LocalDate.now().plusDays(i);//根据传入i，计算需要提醒的书籍的逾期日期

        for (BorrowRecords borrowRecords : brl) {
            LocalDate dueDate = borrowRecords.getDueDate();//获取逾期日期
            if (ReminderDate.equals(dueDate)) {
                String userUUID = borrowRecords.getUuid();
                String emailAddress = userRepository.findByUuid(userUUID).getEmail();//根据UUID获取email地址
                String bookName = bookMapper.findBookById(borrowRecords.getBookId()).getTitle() ;//根据bookId获取书名
                //发送邮件
                emailService.sendTextEmail(emailAddress, "Book overdue reminder", "Your book:"+ bookName + " will overdue in "+ i + "days.");
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
                bookMapper.updateAvailableCopies(borrowRecords.getBookId(), 1);
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

}
