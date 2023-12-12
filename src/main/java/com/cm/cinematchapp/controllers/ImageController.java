package com.cm.cinematchapp.controllers;

import com.cm.cinematchapp.entities.Avatar;
import com.cm.cinematchapp.entities.MoviePoster;
import com.cm.cinematchapp.entities.User;
import com.cm.cinematchapp.services.MovieService;
import com.cm.cinematchapp.services.SecurityService;
import com.cm.cinematchapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * The `ImageController` class is responsible for handling HTTP requests related to user avatars and movie posters
 * This controller essentially provides API endpoints for managing user avatars and movie posters, which appropriate
 * security constraints on who can perform each operation.
 * @author Mateus Souza
 */
@RestController
@RequestMapping(value="/api/images")
@Slf4j
public class ImageController {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @Autowired
    private MovieService movieService;


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @PostMapping("/upload/avatar")
    public ResponseEntity<Avatar> uploadAvatar(
            @RequestParam("file") MultipartFile avatarFile) throws IOException {
        return new ResponseEntity<>(userService.uploadAvatar(avatarFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/avatar")
    public ResponseEntity<byte[]> getAvatar() throws IOException {
        return new ResponseEntity<>(userService.getAvatar(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @DeleteMapping("/avatar")
    public ResponseEntity<Void> deleteAvatar() throws IOException {
        userService.deleteAvatar();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/avatar/{userId}")
    public ResponseEntity<byte[]> getAvatarById(@PathVariable Long userId) throws IOException {
        return new ResponseEntity<>(userService.getAvatarById(userId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/upload/movie/{movie_id}")
    public ResponseEntity<MoviePoster> uploadMoviePoster(
            @PathVariable("movie_id") Long movieId,
            @RequestParam("file") MultipartFile moviePosterFile) throws IOException {
        return new ResponseEntity<>(movieService.uploadPoster(movieId, moviePosterFile), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<byte[]> getMoviePoster(@PathVariable Long movieId) throws IOException {
        return new ResponseEntity<>(movieService.getMoviePosterByMovieId(movieId), HttpStatus.OK);
    }




}
