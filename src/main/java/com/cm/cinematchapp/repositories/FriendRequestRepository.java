package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * The `FriendRequestRepository` interface is responsible for managing friend request data in the database.
 * It extends the JpaRepository interface to perform basic CRUD operations on FriendRequest entities.
 *
 * @author Eric Rebadona
 */
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    /**
     * Retrieve a friend request by its ID.
     *
     * @param requestId The ID of the friend request.
     * @return The FriendRequest entity if found, or null if not found.
     */
    FriendRequest findByRequestId(Long requestId);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE fr.requester.userId = ?1 " +
            "AND fr.recipient.userId = ?2")
    FriendRequest findByRequesterIdAndRecipientId(Long userId, Long friendUserId);


    /**
     * Retrieve a list of friend requests sent by a user based on their user ID.
     *
     * @param userId The ID of the requesting user.
     * @return A list of FriendRequest entities sent by the user.
     */
    List<FriendRequest> getFriendRequestsByRequesterUserId(Long userId);

    /**
     * Retrieve a list of friend requests received by a user based on their user ID.
     *
     * @param userId The ID of the recipient user.
     * @return A list of FriendRequest entities received by the user.
     */
    List<FriendRequest> getFriendRequestsByRecipientUserId(Long userId);


}
