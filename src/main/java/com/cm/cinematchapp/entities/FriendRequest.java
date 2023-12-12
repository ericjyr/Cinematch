package com.cm.cinematchapp.entities;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * The `FriendRequest` entity class represents a friend request between two users in the application.
 * It is used to store information about pending, accepted, or rejected friend requests in the database.
 *
 * @author Eric Rebadona
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"requester_id", "recipient_id"})
})
public class FriendRequest {

    @Id
    @Column(name="request_id", nullable=false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long requestId;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    @Column(name="request_status", nullable=false)
    private FriendRequestStatus requestStatus;

    //date

    public enum FriendRequestStatus {
        PENDING, // Request is pending
        ACCEPTED, // Request has been accepted
        REJECTED // Request has been rejected
    }

}
