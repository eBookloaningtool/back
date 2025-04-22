package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.pojo.Book;

public interface BookService {
    
    /**
     * 根据ID获取电子书详情
     * @param bookId 电子书ID
     * @return 电子书对象，如果不存在则返回null
     */
    Book getBookById(String bookId);
} 