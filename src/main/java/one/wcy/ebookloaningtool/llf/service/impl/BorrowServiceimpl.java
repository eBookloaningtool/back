package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.BookMapper;
import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import one.wcy.ebookloaningtool.llf.response.BorrowResponse;
import one.wcy.ebookloaningtool.llf.response.RenewResponse;
import one.wcy.ebookloaningtool.llf.service.BorrowRecordsRepository;
import one.wcy.ebookloaningtool.llf.service.BorrowService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceimpl implements BorrowService {

    @Autowired
    private final BorrowRecordsRepository borrowRecordsRepository;
    @Autowired
    private BookMapper bookMapper;

    public BorrowServiceimpl(BorrowRecordsRepository borrowRecordsRepository) {
        this.borrowRecordsRepository = borrowRecordsRepository;
    }

    @Override
    public Books findBookById(String bookUUID){
        Books b = bookMapper.findBookById(bookUUID);
        return b;
    }

    //记录借出
    @Override
    public Response recordBorrow(String bookUUID, String userUUID) {

        //判断是否已经有该书的借出记录
        if(checkBorrow(bookUUID, userUUID).isEmpty()){
            //书本库存-1
            bookMapper.updateStock(bookUUID, -1);

            //在BorrowRecords中记录借出
            LocalDateTime dueTime;
            LocalDateTime borrowTime = LocalDateTime.now();
            dueTime = borrowTime.plusDays(30);//一次租借时长30天？
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
            //如果存在则将该书库存+1，将对应记录的status改为“returned"，并记录归还日期,否则返回当前未借阅该书
            bookMapper.updateStock(bookUUID, 1);

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
            LocalDateTime newDueTime = borrowedRecord.getDueDate().plusDays(30);
            borrowedRecord.setDueDate(newDueTime);//暂定固定续借30天？理论上需要前端指定
            borrowRecordsRepository.save(borrowedRecord);
            return new RenewResponse("Renew Successful",newDueTime);
        }else return new Response("The user do not borrow this book.");
    }

    private List<BorrowRecords> checkBorrow(String bookUUID, String userUUID) {
        return borrowRecordsRepository.findByBookUUIDAndUserUUIDAndStatus(bookUUID,userUUID,"borrowed");
    }
}
