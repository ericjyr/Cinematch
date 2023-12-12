package com.cm.cinematchapp.entities;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The `Friendship` entity class represents a friendship relationship between two users.
 * It is used to store information about accepted friendships in the database.
 *
 * @author Eric Rebadona
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class Friendship {

    @Id
    @Column(name="friendship_id", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long friendshipId;

    // Define the two users involved in the friendship
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "friend_user_id", referencedColumnName = "user_id")
    private User friendUser;

    @Enumerated(EnumType.STRING)
    @Column(name="friendship_status", nullable=false)
    private FriendshipStatus friendshipStatus;

    public enum FriendshipStatus {
        ACCEPTED
    }
}
