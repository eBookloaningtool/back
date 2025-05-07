/**
 * Response class for user profile update operations.
 * Contains the updated user information after a successful profile update.
 */
package one.wcy.ebookloaningtool.users.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponse {
    /**
     * Status of the update operation
     */
    private String state;

    /**
     * Unique identifier of the updated user
     */
    private String UUID;

    /**
     * Updated email address of the user
     */
    private String email;

    /**
     * Updated display name of the user
     */
    private String name;
} 