package com.cm.cinematchapp.controllers;

import com.cm.cinematchapp.dto.LoginDTO;
import com.cm.cinematchapp.dto.RegistrationDTO;
import com.cm.cinematchapp.entities.Movie;
import com.cm.cinematchapp.entities.User;
import com.cm.cinematchapp.exceptions.InvalidCredentialsException;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import com.cm.cinematchapp.services.FriendService;
import com.cm.cinematchapp.services.MovieService;
import com.cm.cinematchapp.services.SecurityService;
import com.cm.cinematchapp.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * The `ActionController` class is responsible for handling HTTP requests related to actions.
 * It provides endpoints for different actions to be used in the frontend.
 *
 * @author Eric Rebadona
 */
@RestController
@RequestMapping(value="/api/actions",
        produces="application/json")
@Slf4j
public class ActionController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private MovieService movieService;



    /**
     * Handles HTTP POST requests to create a new user (registration).
     *
     * @param result The validation result for the user data.
     * @return A ResponseEntity containing the created user with an HTTP status of CREATED (201) if successful,
     *         or a ResponseEntity with a status of BAD REQUEST (400) if validation fails.
     */
    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody @Valid RegistrationDTO registrationDTO, BindingResult result) {

        if (result.hasErrors()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(userService.createUser(registrationDTO), HttpStatus.CREATED);
    }

    /**
     * Handles HTTP POST requests for user login.
     *
     * @param loginData The user login data.
     * @param result    The validation result for the login data.
     * @return A ResponseEntity containing a JWT token for successful login with an HTTP status of OK (200),
     *         or a ResponseEntity with a status of BAD REQUEST (400) if validation fails.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginData, BindingResult result) {
        if(result.hasErrors()) return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(securityService.login(loginData.getUsername(), loginData.getPassword()), HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return new ResponseEntity<>(securityService.logout(), HttpStatus.OK);
    }




//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
//    @PostMapping("/upload/avatar")
//    public ResponseEntity<User> uploadAvatar(
//            @RequestParam("file") MultipartFile avatarFile) throws IOException {
//            return new ResponseEntity<>(userService.uploadAvatar(avatarFile), HttpStatus.OK);
//    }




//    @PostMapping("/user/{userId}/add-role/{roleName}")
//    public ResponseEntity<String> addRoleToUser(@PathVariable Long userId, @PathVariable String roleName) {
//        userService.addRoleToUser(userId, roleName);
//        return new ResponseEntity<>("Role added successfully", HttpStatus.OK);
//    }






//    @GetMapping("/movie/search/{title}")
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')") //remove ROLE_USER
//    public ResponseEntity<List<Movie>> searchForMovie(@PathVariable("title") String title) {
//        try {
//            return new ResponseEntity<>(movieService.searchForMovie(title), HttpStatus.OK);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }

    @PostMapping("/movie/search")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')") // Remove ROLE_USER if necessary
    public ResponseEntity<List<Movie>> searchForMovie(@RequestBody String title) {
        try {
            return new ResponseEntity<>(movieService.searchForMovie(title), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/movie/add")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')") //remove ROLE_USER
    public ResponseEntity<?> createMovie(@RequestBody @Valid Movie movie) {
        try {
            return new ResponseEntity<>(movieService.createMovie(movie), HttpStatus.CREATED);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }





}
