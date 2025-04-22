package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import one.wcy.ebookloaningtool.llf.response.BorrowResponse;
import one.wcy.ebookloaningtool.llf.response.RenewResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowRecordsRepository;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.notification.EmailService;
import one.wcy.ebookloaningtool.users.UserRepository;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceimpl implements BorrowService {

    @Autowired
    private final BorrowRecordsRepository borrowRecordsRepository;

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private EmailService emailService;

    //一次租借时长
    private final int BORROW_DURATION = 30;
    //续借时长
    private final int RENEW_DURATION = 30;

    public BorrowServiceimpl(BorrowRecordsRepository borrowRecordsRepository, UserRepository userRepository) {
        this.borrowRecordsRepository = borrowRecordsRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Book findBookById(String bookUUID){
        Book b = bookMapper.findBookById(bookUUID);
        return b;
    }

    //记录借出
    @Override
    public Response recordBorrow(String bookUUID, String userUUID) {

        //判断是否已经有该书的借出记录
        if(checkBorrow(bookUUID, userUUID).isEmpty()){
            //书本库存-1
            bookMapper.updateAvailableCopies(bookUUID, -1);

            //在BorrowRecords中记录借出
            LocalDateTime dueTime;
            LocalDateTime borrowTime = LocalDateTime.now();
            dueTime = borrowTime.plusDays(BORROW_DURATION);
            BorrowRecords borrowRecords = new BorrowRecords();
            borrowRecords.setBookUUID(bookUUID);
            borrowRecords.setUserUUID(userUUID);
            borrowRecords.setBorrowDate(borrowTime);
            borrowRecords.setDueDate(dueTime);
            borrowRecords.setStatus("borrowed");
            borrowRecordsRepository.save(borrowRecords);
            return new BorrowResponse("Borrow Successful", dueTime);
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
            LocalDateTime returnTime = LocalDateTime.now();
            borrowedRecord.setReturnDate(returnTime);
            borrowedRecord.setStatus("returned");
            borrowRecordsRepository.save(borrowedRecord);
            return new Response("Return Successful");
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public Response renewBook(String bookUUID, String userUUID) {

        //判断是否存在正在借出记录
        List<BorrowRecords> brl = checkBorrow(bookUUID,userUUID);
        if (!brl.isEmpty()){
            //如果存在则延长逾期日期，否则返回当前未借阅该书
            BorrowRecords borrowedRecord = brl.getFirst();
            LocalDateTime newDueTime = borrowedRecord.getDueDate().plusDays(RENEW_DURATION);
            borrowedRecord.setDueDate(newDueTime);
            borrowRecordsRepository.save(borrowedRecord);
            return new RenewResponse("Renew Successful",newDueTime);
        }else return new Response("The user do not borrow this book.");
    }

    @Override
    public void overdueReminder(int i) {
        List<BorrowRecords> brl = borrowRecordsRepository.findByStatus("borrowed");
        LocalDate ReminderDate = LocalDate.now().plusDays(i);//根据传入i，计算需要提醒的书籍的逾期日期

        for (BorrowRecords borrowRecords : brl) {
            LocalDate dueDate = borrowRecords.getDueDate().toLocalDate();//获取逾期日期
            if (ReminderDate.equals(dueDate)) {
                String userUUID = borrowRecords.getUserUUID();
                String emailAddress = userRepository.findByUuid(userUUID).getEmail();//根据UUID获取email地址
                String bookName = bookMapper.findBookById(borrowRecords.getBookUUID()).getTitle() ;//根据bookId获取书名
                //发送邮件
                emailService.sendTextEmail(emailAddress, "Book overdue reminder", "Your book:"+ bookName + " will overdue in "+ i + "days.");
            }
        }
    }

    @Override
    public void autoReturn() {
        LocalDate dueDate = LocalDate.now();
        List<BorrowRecords> brl = borrowRecordsRepository.findByStatus("borrowed");
        for (BorrowRecords borrowRecords : brl) {
            if (borrowRecords.getDueDate().toLocalDate().equals(dueDate)) {
                //更改借阅状态
                borrowRecords.setStatus("returned");
                //设置归还时间为当前时间
                borrowRecords.setReturnDate(LocalDateTime.now());
                borrowRecordsRepository.save(borrowRecords);
                //库存+1
                bookMapper.updateAvailableCopies(borrowRecords.getBookUUID(), 1);
            }
        }
    }

    private List<BorrowRecords> checkBorrow(String bookUUID, String userUUID) {
        return borrowRecordsRepository.findByBookUUIDAndUserUUIDAndStatus(bookUUID,userUUID,"borrowed");
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
