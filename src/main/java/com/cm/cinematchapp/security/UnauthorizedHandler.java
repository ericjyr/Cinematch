package com.cm.cinematchapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom Unauthorized Handler for handling unauthorized access exceptions.
 *
 * @author Eric Rebadona
 */
@Component
@Slf4j
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    /**
     * Commence the response when an unauthorized access occurs.
     *
     * @param request                 The HTTP request.
     * @param response                The HTTP response.
     * @param authenticationException The authentication exception.
     * @throws IOException      If there's an I/O exception.
     * @throws ServletException If there's a servlet exception.
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authenticationException) throws IOException, ServletException{

        log.error("Unauthorized Access", authenticationException);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("Unauthorized Access.");
        response.getWriter().flush();

    }


}
