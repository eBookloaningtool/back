/**
 * Mapper interface for Book entity.
 * Provides database operations for managing electronic books,
 * including CRUD operations and search functionality.
 */
package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface BookMapper {

    /**
     * Retrieves a book by its unique identifier.
     * @param id The unique identifier of the book
     * @return The book entity if found, null otherwise
     */
    @Select("select * from Book where bookId=#{id}")
    Book findBookById(String id);

    /**
     * Updates an existing book entity in the database.
     * @param book The book entity containing updated information
     * @return Number of rows affected by the update operation
     */
    @Update("update Book " +
            "set title = #{title}, " +
            "author = #{author}, " +
            "description = #{description}, " +
            "coverUrl = #{coverUrl}, " +
            "availableCopies = #{availableCopies}, " +
            "price = #{price}, " +
            "rating = #{rating}, " +
            "category = #{category}, " +
            "contentURL = #{contentURL}, " +
            "borrowTimes = #{borrowTimes}, " +
            "totalCopies = #{totalCopies} " +
            "Where bookId = #{bookId}")
    int updateBook(Book book);

    /**
     * Retrieves a list of book IDs sorted by borrow count in descending order.
     * Used to identify the most popular books in the system.
     * @return List of book IDs sorted by popularity
     */
    @Select("SELECT bookId FROM Book ORDER BY borrowTimes DESC")
    List<String> findTopPopularBooks();

    /**
     * Searches for books based on title, author, and category criteria.
     * Supports partial matches and flexible search parameters.
     * @param title The title to search for (optional)
     * @param author The author to search for (optional)
     * @param category The category to search for (optional)
     * @return List of book IDs matching the search criteria
     */
    @Select("<script>" +
            "SELECT bookId FROM Book WHERE 1=1" +
            "<if test='title != null'> AND title LIKE CONCAT('%', #{title}, '%')</if>" +
            "<if test='author != null'> AND author LIKE CONCAT('%', #{author}, '%')</if>" +
            "<if test='category != null'> AND category LIKE CONCAT('%', #{category}, '%')</if>" +
            "</script>")
    List<String> searchBooks(String title, String author, String category);
}
