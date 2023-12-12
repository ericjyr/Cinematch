package com.cm.cinematchapp.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Custom implementation of Access Denied Handler for handling access denied exceptions.
 *
 * @author Eric Rebadona
 */
@Component
@Slf4j
public class AccessHandler implements AccessDeniedHandler {

    /**
     * Handle access denied exceptions.
     *
     * @param request              The HTTP request.
     * @param response             The HTTP response.
     * @param accessDeniedException The access denied exception.
     * @throws IOException      If there's an I/O exception.
     * @throws ServletException If there's a servlet exception.
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.error("Access Denied");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println("Access Denied.");
        response.getWriter().flush();
    }
}
