package com.cm.cinematchapp.security;

import cn.hutool.jwt.JWTUtil;
import com.cm.cinematchapp.constants.EntityConstants;
import com.cm.cinematchapp.services.UserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * This filter is responsible for authenticating users based on JWT tokens.
 *
 * @author Eric Rebadona
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    @Autowired
    private UserDetailService userDetailService;

    /**
     * Perform JWT authentication on incoming requests.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If there's a servlet exception.
     * @throws IOException      If there's an I/O exception.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String aHeader = request.getHeader("Authorization");

        // Check if the request has a valid Authorization header.
        if (aHeader ==null || !aHeader.startsWith("Bearer")) {
            // If the header is missing or not starting with "Bearer," proceed with the filter chain.
            filterChain.doFilter(request, response);
            return;
        }

        String aToken = aHeader.split("\\s+")[1];

        log.info("authToken:{}" , aToken);

        // Verify the JWT token's authenticity.
        if(!JWTUtil.verify(aToken, EntityConstants.kSecuritySignKey.getBytes(StandardCharsets.UTF_8))) {
            // If the token is invalid, log the message and continue with the filter chain.
            log.info("token invalid");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("username: " + JWTUtil.parseToken(aToken).getPayload("username"));
        final String username = (String) JWTUtil.parseToken(aToken).getPayload("username");
        UserDetails userDetails = userDetailService.loadUserByUsername(username);

        log.info("Authorities: " + userDetails.getAuthorities());
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Object principal = userDetails.getUsername();
        Object credential = userDetails.getPassword();

        // Create an authentication token for the user.
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(principal, credential, authorities);



        // Set authentication details for the user.
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info(SecurityContextHolder.getContext().toString());

        // Continue with the filter chain after successful authentication.
        filterChain.doFilter(request, response);




    }
}
