package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import one.wcy.ebookloaningtool.utils.Response;

import java.math.BigDecimal;

public interface BorrowService {

    //根据书UUID查找书籍
    Book findBookById(String bookUUID);

    //记录借出
    Response recordBorrow(String bookUUID, String userUUID, BigDecimal price);

    //归还书籍
    Response returnBook(String bookUUID, String userUUID);

    //续借书籍
    Response renewBook(String bookUUID, String userUUID, BigDecimal price);

    //逾期提醒
    void overdueReminder(int i);
    //逾期自动归还
    void autoReturn();

    //获取电子书内容
    String getBookContent(String bookUUID, String userUUID);

    Response getBorrowList(String userID);

    Response getBorrowHistory(String userID);
}
