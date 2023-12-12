package com.cm.cinematchapp.security;

import com.cm.cinematchapp.services.UserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class is responsible for authenticating users and providing users with JWT tokens.
 *
 * @author Eric Rebadona
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailService userDetailService;

    /**
     * Authenticate a user based on the provided credentials.
     *
     * @param authentication The authentication request.
     * @return An authenticated token if the credentials are valid.
     * @throws AuthenticationException If authentication fails.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = String.valueOf(authentication.getPrincipal());
        String password = String.valueOf(authentication.getCredentials());
        log.info("authenticate, username:{}, password:{}", username, password);

        // Load user details for the provided username.
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        log.info(userDetails.getAuthorities().toString());
        // Check if the provided password matches the stored password for the user.
        if(passwordEncoder.matches(password, userDetails.getPassword())) {
            return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
        }

        log.error("Failed Authentication");
        throw new BadCredentialsException("Authentication Error.");
    }

    /**
     * Check if this provider supports the given authentication class.
     *
     * @param authentication The authentication class to check.
     * @return True if supported, false otherwise.
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
