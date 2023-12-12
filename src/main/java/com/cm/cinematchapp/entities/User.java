package com.cm.cinematchapp.entities;

import com.cm.cinematchapp.constants.EntityConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * The `User` entity class represents user information in the application.
 * It stores user details such as first name, last name, username, password, and email.
 * Users can send and receive friend requests and have friendships with other users.
 *
 * @author Eric Rebadona
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @Column(name="user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long userId;

    @Column(name="first_name", nullable=false)
    @NotNull(message="First name cannot be empty.")
    @Size(max=EntityConstants.kMaxNameLen,
        message=EntityConstants.kFirstNameLenViolation)
    private String firstName;

    @Column(name="last_name", nullable=false)
    @NotNull(message="Last name cannot be empty.")
    @Size(max=EntityConstants.kMaxNameLen,
            message=EntityConstants.kLastNameLenViolation)
    private String lastName;

    @Column(name="username", nullable=false, unique=true)
    @NotNull(message="Username cannot be empty.")
    @Size(min=EntityConstants.kMinUsernameLen,
            max=EntityConstants.kMaxUsernameLen,
            message=EntityConstants.kUsernameLenViolation)
    private String username;

    @JsonIgnore
    @Column(name="password", nullable=false)
    @NotNull(message="Password cannot be null.")
    private String password;
    //invalid password and email should still be handled with the client-side to produce a graceful message


    @Column(name="email", nullable=false, unique=true)
    @NotNull(message="Email cannot be empty .")
    @Email
    private String email;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
//    private Avatar avatar;

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "avatar_id") // Assuming you have a foreign key named avatar_id
    private Avatar avatar;



    //private boolean active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;



    @JsonIgnore
    @OneToMany(mappedBy = "requester", cascade = CascadeType.ALL)
    private List<FriendRequest> sentFriendRequests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<FriendRequest> receivedFriendRequests = new ArrayList<>();


    // Define the friendships associated with this user
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Friendship> friendships = new ArrayList<>();


    // Favorite movies of this user
    @ManyToMany
    @JoinTable(
            name = "user_favorite_movies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> favoriteMovies = new HashSet<>();


    @JsonIgnore
    @OneToMany(mappedBy = "user1", cascade = CascadeType.ALL)
    private Set<SharedFavoriteMovies> sharedMoviesAsUser1 = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user2", cascade = CascadeType.ALL)
    private Set<SharedFavoriteMovies> sharedMoviesAsUser2 = new HashSet<>();



}
