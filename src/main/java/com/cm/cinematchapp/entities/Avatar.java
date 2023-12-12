package com.cm.cinematchapp.entities;


import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * The `AvatarEntity` class defines an Entity that is used to store information about user avatars in the database.
 *
 * @author Mateus Souza
 */
@Entity
@Data
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="avatar_id")
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(name="file_name", nullable=false, unique=true)
    private String filename = "";

    @Column(name="path")
    private String path;

    @Column(name="file_size")
    private Long size;







}
