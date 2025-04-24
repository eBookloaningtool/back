package one.wcy.ebookloaningtool.llf.controller;

import one.wcy.ebookloaningtool.llf.service.CategoriesService;
import one.wcy.ebookloaningtool.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
public class CategoryController {
    @Autowired
    private CategoriesService categoriesService;

    @GetMapping("/getAll")
    public Response getAllCategories() {
        return categoriesService.getAllCategories();
    }
}
