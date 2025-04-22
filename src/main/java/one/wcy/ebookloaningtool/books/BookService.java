package one.wcy.ebookloaningtool.books;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import java.util.List;

public interface BookService {
    
    /**
     * 根据ID获取电子书详情
     * @param bookId 电子书ID
     * @return 电子书对象，如果不存在则返回null
     */
    Book getBookById(String bookId);
    
    /**
     * 根据标题、作者、类别搜索电子书
     * @param title 标题关键字
     * @param author 作者关键字
     * @param category 类别关键字
     * @return 电子书ID列表
     */
    List<String> searchBooks(String title, String author, String category);
} 