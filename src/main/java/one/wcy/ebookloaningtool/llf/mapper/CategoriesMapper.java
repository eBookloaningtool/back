/**
 * Mapper interface for Categories entity.
 * Provides database operations for managing book categories.
 */
package one.wcy.ebookloaningtool.llf.mapper;

import one.wcy.ebookloaningtool.llf.pojo.Categories;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoriesMapper {
    /**
     * Retrieves all book categories from the database.
     * @return List of all available categories
     */
    @Select("select * from Categories")
    List<Categories> findAll();
}
