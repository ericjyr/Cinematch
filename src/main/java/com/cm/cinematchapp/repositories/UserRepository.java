package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The `UserRepository` interface is responsible for managing user data in the database.
 * It extends the JpaRepository interface to perform basic CRUD operations on User entities.
 *
 * @author Eric Rebadona
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Retrieve a user by their email address.
     *
     * @param email The email address of the user.
     * @return An Optional containing the user if found, or empty if not.
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if a user with a given email address exists.
     *
     * @param email The email address to check.
     * @return `true` if a user with the email address exists; otherwise, `false`.
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with a given username exists.
     *
     * @param username The username to check.
     * @return `true` if a user with the username exists; otherwise, `false`.
     */
    boolean existsByUsername(String username);

    /**
     * Retrieve a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return An Optional containing the user if found, or empty if not.
     */
    Optional<User> findByUserId(Long userId);

    /**
     * Retrieve a user by their username.
     *
     * @param username The username of the user.
     * @return An Optional containing the user if found, or empty if not.
     */
    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findByUsernameContainingIgnoreCaseAndUserIdNot(String username, Long userId);

    List<User> findByUserIdNot(Long userId);

    @Query("SELECT CASE WHEN COUNT(u.avatar) > 0 THEN true ELSE false END FROM User u WHERE u.userId = :userId")
    boolean avatarExistByUserId(@Param("userId") Long userId);

}
