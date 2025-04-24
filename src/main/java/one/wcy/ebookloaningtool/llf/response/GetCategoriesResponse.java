package one.wcy.ebookloaningtool.llf.response;

import lombok.Getter;
import lombok.Setter;
import one.wcy.ebookloaningtool.llf.pojo.Categories;
import one.wcy.ebookloaningtool.utils.Response;

import java.util.List;

@Getter
@Setter
public class GetCategoriesResponse extends Response {
    private List<Categories> categoriesList;

    public GetCategoriesResponse(String state, List<Categories> categoriesList) {
        super(state);
        this.categoriesList = categoriesList;
    }
}
