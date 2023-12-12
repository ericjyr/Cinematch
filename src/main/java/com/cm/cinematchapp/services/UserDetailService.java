package com.cm.cinematchapp.services;

import com.cm.cinematchapp.entities.User;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import com.cm.cinematchapp.security.UserDetailsImpl;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.cm.cinematchapp.repositories.UserRepository;

/**
 * The `UserDetailService` class is responsible for providing user details to the Spring Security framework.
 * It implements the Spring Security `UserDetailsService` interface, allowing user details to be loaded by their username.
 *
 * @author Eric Rebadona
 */
@Service
@Transactional
@Slf4j
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details by the given username.
     *
     * @param username The username of the user.
     * @return A `UserDetails` object representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            log.debug("load user:{}", username);
            User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(
                    () -> new ResourceNotFoundException("No user found with username= " + username));
            return new UserDetailsImpl(user);
        } catch (ResourceNotFoundException e) {
            throw new UsernameNotFoundException("No user found with username= " + username);
        }
    }

}

