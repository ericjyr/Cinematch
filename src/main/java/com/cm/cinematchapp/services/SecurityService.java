package com.cm.cinematchapp.services;

import cn.hutool.jwt.JWT;
import com.cm.cinematchapp.constants.EntityConstants;
import com.cm.cinematchapp.entities.User;
import com.cm.cinematchapp.exceptions.InvalidCredentialsException;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import com.cm.cinematchapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

/**
 * The `SecurityService` class provides authentication and security-related functionality.
 * It allows users to log in, log out, and retrieve the current logged-in user.
 *
 * @author Eric Rebadona
 */
@Service
@Transactional
@Slf4j
public class SecurityService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    /**
     * Logs in a user with the given username and password.
     *
     * @param username The username of the user.
     * @param password The user's password.
     * @return The authentication token upon successful login.
     */
    public String login(String username, String password){

        if (username.length() == 0 || username == null) {
            log.error("Username is null");
        }

        if (password.length() == 0 || password == null) {
            log.error("Password is null");
        }

        if (!userRepository.existsByUsername(username)) {
            throw new ResourceNotFoundException("Sorry, could not find an account with the provided username.");
        }

        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);


            authenticationManager.authenticate(authenticationToken);

            Date expireTime = new Date(System.currentTimeMillis() + EntityConstants.kSessionTimeout);
            byte[] signKey = EntityConstants.kSecuritySignKey.getBytes(StandardCharsets.UTF_8);

            String token = JWT.create()
                    .setExpiresAt(expireTime)
                    .setPayload("username", username)
                    .setKey(signKey)
                    .sign();

            return token;
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException("Oops! It seems like the password is incorrect. Please try again.");
        }
    }


    /**
     * Logs out the currently authenticated user.
     *
     * @return An empty string to indicate a successful logout.
     */
    public String logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return "Successful Log Out";
    }

    /**
     * Retrieves the currently logged-in user.
     *
     * @return An optional `User` object representing the current user.
     */
    public Optional<User> getCurrentLoginUser() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsernameIgnoreCase(username);
    }
    /**
     * Retrieves the user ID of the currently authenticated user.
     * This method uses Spring Security's SecurityContextHolder to obtain the
     * username of the currently authenticated user and then queries the UserRepository
     * to fetch the corresponding user entity and retrieve the user ID.
     *
     * @return The user ID of the currently authenticated user.
     */
    public Long getCurrentLoginUserId() {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();


        return userRepository.findByUsernameIgnoreCase(username).get().getUserId();
    }

}
