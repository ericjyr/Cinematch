package com.cm.cinematchapp.security;

import com.cm.cinematchapp.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom UserDetails implementation for user authentication and authorization.
 *
 * @author Eric Rebadona
 */
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private final User user;

    /**
     * Get the collection of granted authorities for the user. (Not implemented)
     *
     * @return The collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Get the user's password.
     *
     * @return The user's password.
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * Get the user's username.
     *
     * @return The user's username.
     */
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * Check if the user's account is non-expired.
     *
     * @return True if the user's account is non-expired.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true; // Set to true to pass as Cinematch accounts will not expire.
    }

    /**
     * Check if the user's account is non-locked.
     *
     * @return True if the user's account is non-locked.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true; // Implement logic if we decide to be able to lock an account
    }

    /**
     * Check if the user's credentials are non-expired.
     *
     * @return True if the user's credentials are non-expired.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Set to true to pass as credentials will not expire.
    }

    /**
     * Check if the user's account is enabled.
     *
     * @return True if the user's account is enabled.
     */
    @Override
    public boolean isEnabled() {
        return true; // Implement logic if we decide accounts can be disabled.
    }
}
