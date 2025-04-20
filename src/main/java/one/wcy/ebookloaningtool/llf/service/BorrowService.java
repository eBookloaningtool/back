package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.llf.pojo.Books;
import one.wcy.ebookloaningtool.utils.Response;

public interface BorrowService {

    //根据书UUID查找书籍
    Books findBookById(String bookUUID);

    //记录借出
    Response recordBorrow(String bookUUID, String userUUID);

    //归还书籍
    Response returnBook(String bookUUID, String userUUID);

    //续借书籍
    Response renewBook(String bookUUID, String userUUID);

    void overdueReiminder(int i);
}
