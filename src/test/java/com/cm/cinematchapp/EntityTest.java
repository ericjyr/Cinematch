//package com.cm.cinematchapp;
//
//import com.cm.cinematchapp.entities.FriendRequest;
//import com.cm.cinematchapp.entities.Friendship;
//import com.cm.cinematchapp.entities.User;
//import com.cm.cinematchapp.exceptions.DuplicateObjectException;
//import com.cm.cinematchapp.repositories.FriendRequestRepository;
//import com.cm.cinematchapp.repositories.FriendshipRepository;
//import com.cm.cinematchapp.repositories.UserRepository;
//import com.cm.cinematchapp.services.FriendService;
//import com.cm.cinematchapp.services.UserService;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.dao.DataIntegrityViolationException;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//class EntityTest {
//
//    @Autowired
//    private TestEntityManager testEntityManager;
//
//    @InjectMocks
//    private UserService userService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private FriendService friendService;
//
//    @Mock
//    private FriendRequestRepository friendRequestRepository;
//
//    @Mock
//    private FriendshipRepository friendshipRepository;
//
//    private User user;
//    private User user2;
//
//
//
//    /**
//     * @throws java.lang.Exception
//     */
//    @BeforeEach
//    void setUp() throws Exception {
//        MockitoAnnotations.openMocks(this); // Initialize mocks
//
//        // Sample user 1
//        user = new User();
//        user.setFirstName("John");
//        user.setLastName("Doe");
//        user.setUsername("johndoe");
//        user.setPassword("ValidPass123");
//        user.setEmail("johndoe@example.com");
//
//        // Sample user 2
//        user2 = new User();
//        user2.setFirstName("Jane");
//        user2.setLastName("Doe");
//        user2.setUsername("janedoe");
//        user2.setPassword("password");
//        user2.setEmail("janedoe@example.com");
//
//        // Mock the behavior of userRepository.save() to return the user object
//        when(userRepository.save(user)).thenReturn(user);
//        when(userRepository.save(user2)).thenReturn(user2);
//
//    }
//
//    @AfterEach
//    void tearDown() {
//        testEntityManager.clear();
//    }
//
//
//    /**
//     * Test method for {@link com.cm.cinematchapp.services.UserService#createUser(User)}
//     */
//    @Test
//    void testCreateValidUser() {
//
//        // Create a user object for testing
//        user = new User();
//        user.setFirstName("Juan");
//        user.setLastName("Doe");
//        user.setUsername("juandoe");
//        user.setPassword("password");
//        user.setEmail("juandoe@example.com");
//
//        // Mock the behavior of userRepository.save() to return the user object
//        when(userRepository.save(user)).thenReturn(user);
//
//        // Call the createUser method
//        User createdUser = userService.createUser(user);
//
//        // Verify that userRepository.save() was called with the user object
//        verify(userRepository, times(1)).save(user);
//
//        // Check if the returned user matches the original user
//        assertEquals("Juan", createdUser.getFirstName());
//        assertEquals("Doe", createdUser.getLastName());
//        assertEquals("juandoe", createdUser.getUsername());
//        assertEquals("password", createdUser.getPassword());
//        assertEquals("juandoe@example.com", createdUser.getEmail());
//    }
//
//
//    /**
//     * Test checking email existence.
//     */
//    @Test
//    void testExistsByEmail() {
//
//        // Mock the behavior of existsByEmail
//        when(userRepository.existsByEmail("johndoe@example.com")).thenReturn(true);
//        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);
//
//        // Check if email exists
//        assertTrue(userRepository.existsByEmail("johndoe@example.com"));
//        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
//    }
//
//    /**
//     * Test creating a user with a duplicate email, which should throw an exception.
//     */
//    @Test
//    void testCreateUserWithDuplicateEmailThrowsException() {
//
//        // Create a new user with the same email
//        User newUser = new User();
//        newUser.setFirstName("Johnny");
//        newUser.setLastName("Doe");
//        newUser.setUsername("johnnydoe");
//        newUser.setPassword("NewPass123");
//        newUser.setEmail("johndoe@example.com"); // Duplicate email
//
//        when(userRepository.existsByEmail("johndoe@example.com")).thenReturn(true);
//
//        // Ensure that creating the user with a duplicate email throws an exception
//        assertThrows(DuplicateObjectException.class, () -> {
//            userService.createUser(newUser);
//        });
//
//
//
//        // Verify that userRepository.save() was not called for the new user
//        verify(userRepository, never()).save(newUser);
//    }
//
//
//    /**
//     * Test creating a user with an invalid email, which should throw an exception.
//     */
//    @Test
//    void testCreateUserWithInvalidEmailThrowsException() {
//        User user = new User();
//        user.setFirstName("Johnny");
//        user.setLastName("Doe");
//        user.setUsername("johnnydoe");
//        user.setPassword("password");
//        user.setEmail("invalid-email"); // Invalid email
//
//        assertThrows(DataIntegrityViolationException.class, () -> {
//            userService.createUser(user);
//        });
//
//        verify(userRepository, never()).save(user);
//    }
//
//    void testCreateUserWithInvalidPasswordPattern() {
//        User user = new User();
//        user.setFirstName("Johnny");
//        user.setLastName("Doe");
//        user.setUsername("johnnydoe");
//        user.setPassword("password");
//        user.setEmail("johnnydoe@gmail.com");
//    }
//
//
//    /**
//     * Test getting friend requests by recipient ID.
//     */
//    @Test
//    void testGetFriendRequestsByRecipientId() {
//
//        // Simulate saving the friend request. user sends request to user2.
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setRequester(user);
//        friendRequest.setRecipient(user2);
//        friendRequest.setRequestStatus(FriendRequest.FriendRequestStatus.PENDING);
//
//        // Get the IDs of the sender (requester) and recipient.
//        Long requesterId = user.getUserId();
//        Long recipientId = user2.getUserId();
//
//        // Mock the behavior of friendRequestRepository.save() to return the friendRequest object.
//        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(friendRequest);
//        // Mock the behavior of findUserId
//        when(userRepository.findByUserId(requesterId)).thenReturn(Optional.of(user));
//        when(userRepository.findByUserId(recipientId)).thenReturn(Optional.of(user2));
//
//        // Send the friend request
//        FriendRequest sentFriendRequest = friendService.sendFriendRequest(recipientId);
//
//        // Simulate getting friend requests by user ID
//        when(friendRequestRepository.getFriendRequestsByRecipientUserId(recipientId)).thenReturn(List.of(sentFriendRequest));
//        List<FriendRequest> friendRequests = friendService.getFriendRequestsByRecipientId(recipientId);
//
//        // Assertions
//        assertEquals(1, friendRequests.size());
//        FriendRequest friendRequestFromDB = friendRequests.get(0);
//        assertEquals(requesterId, friendRequestFromDB.getRequester().getUserId());
//        assertEquals(recipientId, friendRequestFromDB.getRecipient().getUserId());
//    }
//
//
//
//    /**
//     * Test accepting a friend request and creating friendships accordingly.
//     */
//    @Test
//    void testAcceptFriendRequestAndCreatingFriendships() {
//        // Simulate saving the friend request. user sends request to user2.
//        FriendRequest friendRequest = new FriendRequest();
//        friendRequest.setRequester(user);
//        friendRequest.setRecipient(user2);
//        friendRequest.setRequestStatus(FriendRequest.FriendRequestStatus.PENDING);
//
//        // Get the IDs of the sender (requester) and recipient.
//        Long requesterId = user.getUserId();
//        Long recipientId = user2.getUserId();
//
//        // Mock the behavior of friendRequestRepository.save() to return the friendRequest object.
//        when(friendRequestRepository.save(any(FriendRequest.class))).thenReturn(friendRequest);
//
//        // Mock the behavior of findUserId
//        when(userRepository.findByUserId(requesterId)).thenReturn(Optional.of(user));
//        when(userRepository.findByUserId(recipientId)).thenReturn(Optional.of(user2));
//
//        FriendRequest sentFriendRequest = friendService.sendFriendRequest(recipientId);
//
//        // Mock the behavior of friendRequestRepository.getByRequestId() to return the sentFriendRequest
//        when(friendRequestRepository.findByRequestId(sentFriendRequest.getRequestId())).thenReturn(sentFriendRequest);
//
//        friendService.acceptFriendRequest(sentFriendRequest.getRequestId());
//
//        // Retrieve the updated friend request from the repository
//        FriendRequest updatedFriendRequest = friendRequestRepository.findByRequestId(sentFriendRequest.getRequestId());
//
//        // Verify that the friend request status is updated to ACCEPTED
//        assertEquals(FriendRequest.FriendRequestStatus.ACCEPTED, updatedFriendRequest.getRequestStatus());
//
//        // Verify that a friendship entry is created in the repository
//        verify(friendshipRepository, times(2)).save(any(Friendship.class));
//
//        // Additional Assertions:
//        // Verify that the user IDs in the friendship entry match the sender and recipient IDs
//
//        // Create a list of Friendship objects to return when findByUserUserId is called for requester
//        List<Friendship> expectedFriendshipsRequester = new ArrayList<>();
//        Friendship friendshipRequester = new Friendship();
//        friendshipRequester.setUser(user);
//        friendshipRequester.setFriendUser(user2);
//        friendshipRequester.setFriendshipStatus(Friendship.FriendshipStatus.ACCEPTED);
//        expectedFriendshipsRequester.add(friendshipRequester);
//
//        // Mock the behavior of the findByUserUserId method for requester
//        when(friendshipRepository.findByUserUserId(requesterId)).thenReturn(expectedFriendshipsRequester);
//
//        // Create a list of Friendship objects to return when findByUserUserId is called for recipient
//        List<Friendship> expectedFriendshipsRecipient = new ArrayList<>();
//        Friendship friendshipRecipient = new Friendship();
//        friendshipRecipient.setUser(user2);
//        friendshipRecipient.setFriendUser(user);
//        friendshipRecipient.setFriendshipStatus(Friendship.FriendshipStatus.ACCEPTED);
//        expectedFriendshipsRecipient.add(friendshipRecipient);
//
//        // Mock the behavior of the findByUserUserId method for recipient
//        when(friendshipRepository.findByUserUserId(recipientId)).thenReturn(expectedFriendshipsRecipient);
//
//        // Now, let's check if the expected friendships are saved for both users
//        List<Friendship> savedFriendshipsRequester = friendshipRepository.findByUserUserId(requesterId);
//        List<Friendship> savedFriendshipsRecipient = friendshipRepository.findByUserUserId(recipientId);
//
//        assertEquals(1, savedFriendshipsRequester.size());
//        assertEquals(1, savedFriendshipsRecipient.size());
//    }
//
//
//}
