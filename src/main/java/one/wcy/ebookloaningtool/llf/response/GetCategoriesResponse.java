/**
 * Response class for retrieving book categories.
 * Extends the base Response class to include a list of available book categories.
 */
package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.Categories;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetCategoriesResponse extends Response {
    /**
     * List of all available book categories
     */
    private List<Categories> categoriesList;

    /**
     * Constructs a new GetCategoriesResponse with the specified state and categories.
     * @param state The response state indicating success or failure
     * @param categoriesList The list of available book categories
     */
    public GetCategoriesResponse(String state, List<Categories> categoriesList) {
        super(state);
        this.categoriesList = categoriesList;
    }
}
