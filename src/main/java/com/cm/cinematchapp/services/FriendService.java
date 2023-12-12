package com.cm.cinematchapp.services;

import com.cm.cinematchapp.entities.FriendRequest;
import com.cm.cinematchapp.entities.Friendship;
import com.cm.cinematchapp.entities.User;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import com.cm.cinematchapp.repositories.FriendRequestRepository;
import com.cm.cinematchapp.repositories.FriendshipRepository;
import com.cm.cinematchapp.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * The `FriendService` class manages friend requests and friendships between users.
 * It provides methods to send friend requests, accept friend requests, get friend requests
 * and friendships for a user, and remove friendships.
 *
 * @author Eric Rebadona
 */
@Service
@Transactional
@Slf4j
public class FriendService {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SecurityService securityService;


    // --- Friend Requests ---

    // This section contains methods related to friend requests.

    /**
     * Get a list of friend requests by recipient ID.
     *
     * @return A list of friend requests for the recipient.
     */
    public List<FriendRequest> getFriendRequests() {
        return friendRequestRepository.getFriendRequestsByRecipientUserId(securityService.getCurrentLoginUserId());
    }

    /**
     * Send a friend request.
     *
     * @param recipientId The ID of the recipient user.
     * @return The created friend request.
     */
    public FriendRequest sendFriendRequest(Long recipientId) {
        // Retrieve the requester and the recipient by their IDs
        User requester = securityService.getCurrentLoginUser().get();
        User recipient = userRepository.findByUserId(recipientId).get();

//        if (friendshipRepository.findByUserIdAndFriendUserId(requester.getUserId(), recipientId) != null) {
//            throw new DuplicateObjectException("Users are already friends");
//        }

        // Create a new friend request
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setRecipient(recipient);
        friendRequest.setRequestStatus(FriendRequest.FriendRequestStatus.PENDING);

        // Save the friend request to the repository
        return friendRequestRepository.save(friendRequest);
    }
    /**
     * Removes a friend request.
     *
     * @param recipientId The ID of the recipient user.
     */

    public void removeFriendRequest(Long recipientId) {
        // Retrieve the requester (logged-in user) and the recipient by their IDs
        Long requesterId = securityService.getCurrentLoginUserId();

        // Check if there's a friend request from the requester to the recipient
        FriendRequest friendRequest = friendRequestRepository.findByRequesterIdAndRecipientId(requesterId, recipientId);

        if (friendRequest != null) {
            // If a friend request exists, delete it
            friendRequestRepository.delete(friendRequest);
        } else {
            // Handle the case when there is no friend request
            throw new ResourceNotFoundException("Friend request not found");
        }
    }


    /**
     * Accept a friend request.
     *
     * @param requestId The ID of the friend request to accept.
     */
    public void acceptFriendRequest(Long requestId) {

        // Retrieve the friend request by its ID from the repository, or set it to null if not found
        FriendRequest friendRequest = friendRequestRepository.findByRequestId(requestId);

        // Check if the friend request exists and is in a PENDING status
        if (friendRequest != null && friendRequest.getRequestStatus() == FriendRequest.FriendRequestStatus.PENDING) {
            // Update the request status to "Accepted"
            friendRequest.setRequestStatus(FriendRequest.FriendRequestStatus.ACCEPTED);
            friendRequestRepository.save(friendRequest);

            // Create an entry in the Friendship table for userA (the requester)
            Friendship friendshipA = new Friendship();
            friendshipA.setUser(friendRequest.getRequester());
            friendshipA.setFriendUser(friendRequest.getRecipient());
            friendshipA.setFriendshipStatus(Friendship.FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendshipA);

            // Create an entry in the Friendship table for userB (the recipient)
            Friendship friendshipB = new Friendship();
            friendshipB.setUser(friendRequest.getRecipient());
            friendshipB.setFriendUser(friendRequest.getRequester());
            friendshipB.setFriendshipStatus(Friendship.FriendshipStatus.ACCEPTED);
            friendshipRepository.save(friendshipB);

            // Remove the friend request from the FriendRequest table
            friendRequestRepository.delete(friendRequest);
        }
    }

    public void denyFriendRequest(Long requestId) {

        FriendRequest friendRequest = friendRequestRepository.findByRequestId(requestId);

        if (friendRequest != null) {
            // If a friend request exists, delete it
            friendRequestRepository.delete(friendRequest);
        } else {
            // Handle the case when there is no friend request
            throw new ResourceNotFoundException("Friend request not found");
        }
    }




    // --- Friendships ---

    // This section contains methods related to friendships.

    /**
     * Get a list of friendships by user ID.
     *
     * @return A list of friends(Users) for the user.
     */
    public List<User> getFriends() {
        return friendshipRepository.findFriendUserByUserId(securityService.getCurrentLoginUserId());
    }



    public void removeFriend(Long friendUserId) {

        User friendUser = userRepository.findByUserId(friendUserId).orElse(null);
        User user = securityService.getCurrentLoginUser().get();

        // Find the friendship record to delete for user A
        Friendship friendship1 = friendshipRepository.findByUserAndFriendUser(user, friendUser);

        // Find the friendship record to delete for user B
        Friendship friendship2 = friendshipRepository.findByUserAndFriendUser(friendUser, user);

        if (friendship1 != null && friendship2 != null) {
            // Delete both friendship records (bi-directional)
            friendshipRepository.delete(friendship1);
            friendshipRepository.delete(friendship2);
            log.info("Friendship removed between user with ID {} and user with ID {}", user.getUserId(), friendUser.getUserId());
        } else {
            log.warn("No friendship found between user with ID {} and user with ID {}", user.getUserId(), friendUser.getUserId());
        }
    }



}
