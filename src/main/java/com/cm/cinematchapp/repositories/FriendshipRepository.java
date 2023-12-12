package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.Friendship;
import com.cm.cinematchapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The `FriendshipRepository` interface is responsible for managing friendship data in the database.
 * It extends the JpaRepository interface to perform basic CRUD operations on Friendship entities.
 *
 * @author Eric Rebadona
 */
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    /**
     * Retrieve a friendship by its ID.
     *
     * @param friendshipId The ID of the friendship.
     * @return The Friendship entity if found, or null if not found.
     */
    Friendship findByFriendshipId(Long friendshipId);

    /**
     * Retrieve a list of friendships associated with a user by their user ID.
     *
     * @param userId The ID of the user.
     * @return A list of Friendship entities associated with the user.
     */
    List<Friendship> findByUserUserId(Long userId);

    /**
     * Retrieve a friendship by the user's ID and their friend's ID.
     *
     * @param userId The ID of the user.
     * @param friendUserId The ID of the friend.
     * @return The Friendship entity if found, or null if not found.
     */
    @Query("SELECT f FROM Friendship f WHERE f.user.userId = ?1 AND f.friendUser.userId = ?2")
    Friendship findByUserIdAndFriendUserId(Long userId, Long friendUserId);

    Friendship findByUserAndFriendUser(User user, User friendUser);

    @Query("SELECT f.friendUser FROM Friendship f WHERE f.user.userId = ?1")
    List<User> findFriendUserByUserId(Long userId);



}
