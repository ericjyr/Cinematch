package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 * The `RoleRepository` interface is responsible for finding users that match a specific role in the database..
 * It extends the JpaRepository interface
 *
 * @author Mateus Souza
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Retrieve a role by its name on the database.
     *
     * @param Name The name of the role in the database.
     * @return The Role entity if found, or null if not found.
     */
    Role findByName(String Name);
}
