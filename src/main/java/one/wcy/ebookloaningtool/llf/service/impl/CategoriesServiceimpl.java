/**
 * Implementation of the CategoriesService interface.
 * Provides concrete implementation for retrieving book categories from the database.
 */
package one.wcy.ebookloaningtool.llf.service.impl;

import one.wcy.ebookloaningtool.llf.mapper.CategoriesMapper;
import one.wcy.ebookloaningtool.llf.pojo.Categories;
import one.wcy.ebookloaningtool.llf.response.GetCategoriesResponse;
import one.wcy.ebookloaningtool.llf.service.CategoriesService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriesServiceimpl implements CategoriesService {
    /**
     * Mapper for database operations related to book categories
     */
    @Autowired
    private CategoriesMapper categoriesMapper;

    /**
     * Retrieves all book categories from the database.
     * @return Response containing the list of all categories
     */
    @Override
    public Response getAllCategories() {
        List<Categories> categoriesList = categoriesMapper.findAll();
        return new GetCategoriesResponse("Success", categoriesList);
    }
}
