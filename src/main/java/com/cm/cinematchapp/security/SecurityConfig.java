package com.cm.cinematchapp.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * Configuration class for security settings in the application.
 *
 * @author Eric Rebadona
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

    //Autowired Components
    @Autowired
    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Autowired
    private CorsConfig corsConfig;


//    @Bean
//    public RoleHierarchy roleHierarchy() {
//        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
//        String hierarchy = "ROLE_ADMIN > ROLE_USER";
//        roleHierarchy.setHierarchy(hierarchy);
//        return roleHierarchy;
//    }
//
//    @Bean
//    public DefaultWebSecurityExpressionHandler webSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
//        expressionHandler.setRoleHierarchy(roleHierarchy());
//        return expressionHandler;
//    }

    /**
     * Configuration class for security settings in the application.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Create and configure an AuthenticationManager.
     *
     * @param authConfig AuthenticationConfiguration instance.
     * @return The configured AuthenticationManager.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Create an instance of JwtAuthenticationFilter.
     *
     * @return An instance of JwtAuthenticationFilter.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthorizationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Create an instance of JwtAuthenticationProvider.
     *
     * @return An instance of JwtAuthenticationProvider.
     */
    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider();
    }

    /**
     * Configure the security filter chain for HTTP requests.
     *
     * @param httpSecurity The HttpSecurity instance to configure.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // Disable CSRF protection
                .csrf(csrf -> csrf.disable())
                // Set session management to STATELESS. Ensures session-related info is not stored in server-side
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // If you create a class meant for any guest methods like swiping it'll need to be allowed below
                // Used to allow access for certain entry points to unauthorized users.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers("/api/actions/register", "/api/actions/login").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .requestMatchers("/api/test/**").permitAll()
                        .requestMatchers("/api/actions/user/**", "/api/entities/user/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/api/entities/users", "/api/entities/users").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())
                // Set Cross-Origin Resource Sharing defined in corsConfig
                .cors(cors -> cors.configurationSource(corsConfig));

        log.debug("Request paths matched:");

        // TODO maybe disable caching for headers?

        // Set the JWT authentication provider
        httpSecurity.authenticationProvider(jwtAuthenticationProvider());

        // Set JWT authentication filter before UsernamePasswordAuthenticationFilter
        httpSecurity.addFilterBefore(jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

        // Configure Exception handlers
        httpSecurity.exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(unauthorizedHandler));

        // Permit all logout requests. Removes session / JWT token.
        httpSecurity.logout(lo -> lo.permitAll());

        return httpSecurity.build();
    }


}
