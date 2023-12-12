package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The `AvatarRepository` interface is responsible for managing user avatar info in the database.
 * It extends the JpaRepository interface
 *
 * @author Mateus Souza
 */
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    /**
     * Retrieve a user avatar filename and path in the database.
     *
     * @param fileName The path for the user avatar in the database.
     * @return The avatar entity if found, or null if not found.
     */
    Avatar findByFilename(String fileName);
    Avatar findByPath(String path);
}
