/**
 * Entity class representing a book category.
 * Contains basic information about a category including its name and description.
 */
package one.wcy.ebookloaningtool.llf.pojo;

import lombok.Data;


@Data
public class Categories {
    /**
     * The name of the category
     */
    String name;

    /**
     * A detailed description of the category
     */
    String description;
}
