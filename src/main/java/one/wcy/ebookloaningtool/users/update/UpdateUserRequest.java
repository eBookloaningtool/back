/**
 * Request class for user profile update operations.
 * Contains the fields that can be updated in a user's profile.
 */
package one.wcy.ebookloaningtool.users.update;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    /**
     * New email address for the user
     */
    private String email;

    /**
     * New password for the user
     */
    private String password;

    /**
     * New display name for the user
     */
    private String name;
} 