/**
 * Service interface for managing book categories.
 * Provides methods for retrieving category information.
 */
package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.utils.Response;

public interface CategoriesService {
    /**
     * Retrieves all available book categories from the system.
     * @return Response containing the list of all categories
     */
    Response getAllCategories();
}
