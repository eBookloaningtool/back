/**
 * Repository interface for User entity.
 * Provides methods for accessing and managing user data in the database.
 */
package one.wcy.ebookloaningtool.users;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Finds a user by their email address.
     *
     * @param email The email address to search for
     * @return The user with the specified email, or null if not found
     */
    User findByEmail(String email);

    /**
     * Finds a user by their display name.
     *
     * @param name The name to search for
     * @return The user with the specified name, or null if not found
     */
    User findByName(String name);

    /**
     * Finds a user by their UUID.
     *
     * @param uuid The UUID to search for
     * @return The user with the specified UUID, or null if not found
     */
    User findByUuid(String uuid);
}
