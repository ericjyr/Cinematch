package com.cm.cinematchapp.services;


import com.cm.cinematchapp.constants.EntityConstants;
import com.cm.cinematchapp.entities.*;
import com.cm.cinematchapp.exceptions.ResourceNotFoundException;
import com.cm.cinematchapp.repositories.MoviePosterRepository;
import com.cm.cinematchapp.repositories.MovieRepository;
import com.cm.cinematchapp.repositories.SharedFavoriteMoviesRepository;
import com.cm.cinematchapp.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.stream.Collectors;

import static com.cm.cinematchapp.constants.EntityConstants.kPostersPath;

@Service
@Transactional
@Slf4j
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private MoviePosterRepository moviePosterRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SharedFavoriteMoviesRepository sharedFavoriteMoviesRepository;

    @Autowired
    private SecurityService securityService;


    @Autowired
    private RestTemplate restTemplate;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public List<Movie> searchForMovie(String title) throws JsonProcessingException {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-RapidAPI-Key", EntityConstants.kRapidApiKey);
        httpHeaders.set("X-RapidAPI-Host", EntityConstants.kRapidApiHost);

        HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(EntityConstants.kRapidApiUrl)
                .queryParam("title", title.replace(" ", "%20"))
                .queryParam("country", "ca")
                .queryParam("show_type", "movie")
                .queryParam("output_language", "en");

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        String responseBody = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        MovieResult movieResult = objectMapper.readValue(responseBody, MovieResult.class);

        List<Movie> allMovies = movieResult.getResult();
        List<Movie> movies = new ArrayList<>();

        int movieCount = Math.min(5, allMovies.size()); // Limit to the first 5 movies

        for (int i = 0; i < movieCount; i++) {
            movies.add(allMovies.get(i));
        }

        return movies;
    }

    public Movie createMovie(Movie movie) throws IOException {

        HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
        String encodedTitle = URLEncoder.encode(movie.getTitle(), StandardCharsets.UTF_8.toString());
        String url = EntityConstants.kOMDBApiHost + encodedTitle + "&y=" + movie.getYear() + EntityConstants.kOMDBApiKey;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        Map<String, Object> responseBody = responseEntity.getBody();

        Movie createdMovie = new Movie();

        createdMovie.setTitle(movie.getTitle());
        createdMovie.setYear(movie.getYear());
        createdMovie.setStreamingInfo(movie.getStreamingInfo());

        if (responseBody != null) {
            createdMovie.setDescription((String) responseBody.get("Plot"));
            createdMovie.setRated((String) responseBody.get("Rated"));
            String posterUrl = (String) responseBody.get("Poster");
            MoviePoster moviePoster = downloadPoster(posterUrl); // Save poster image and get the MoviePoster object
            moviePoster = moviePosterRepository.save(moviePoster);
            createdMovie.setPoster(moviePoster);
        }

        createdMovie = movieRepository.save(createdMovie);

        return createdMovie;
    }

    public void deleteMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);

        if (movie != null) {
            MoviePoster poster = movie.getPoster();

            if (poster != null) {
                movie.setPoster(null);
                moviePosterRepository.delete(poster);
            }

            movieRepository.delete(movie);
        } else {
            throw new ResourceNotFoundException("Movie does not exist");
        }
    }


    public byte[] getMoviePosterByMovieId(Long movieId) throws IOException {
        Movie movie = movieRepository.getMovieById(movieId);
        MoviePoster moviePoster = movie.getPoster();
        Path posterPath = Paths.get(moviePoster.getPath());
        return Files.readAllBytes(posterPath);
//        else {
//            moviePoster = moviePosterRepository.findByFilename("default_poster.png");
//            Path defaultPath = Paths.get(moviePoster.getPath());
//            return Files.readAllBytes(defaultPath);
//        }

    }

    private MoviePoster downloadPoster(String posterUrl) throws IOException {
        URL url = new URL(posterUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream inputStream = connection.getInputStream()) {
            String fileName = generateUniqueFileName(); // Function to generate a unique file name
            String filePath = kPostersPath + fileName;

            // Save the image data to a local file
            try (OutputStream outputStream = new FileOutputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            File posterFile = new File(filePath);
            long fileSize = posterFile.length();

            MoviePoster moviePoster = new MoviePoster();
            moviePoster.setFilename(fileName);
            moviePoster.setPath(filePath);
            moviePoster.setSize(fileSize);

            return moviePoster;
        }
    }


    public MoviePoster uploadPoster(Long movieId, MultipartFile posterFile) throws IOException {

        Movie movie = movieRepository.getMovieById(movieId);

        //TODO change to cloud? something else rather than local file?

        String fileName = generateUniqueFileName();
        String filePath = kPostersPath + fileName;
        Path localPath = Paths.get(filePath);

        Files.copy(posterFile.getInputStream(), localPath, StandardCopyOption.REPLACE_EXISTING);

        MoviePoster poster = new MoviePoster();
        poster.setFilename(fileName);
        poster.setPath(filePath);
        poster.setSize(posterFile.getSize());
        poster = moviePosterRepository.save(poster);

        if (movieRepository.posterExistByMovieId(movieId)) {
            deletePoster(movie);
        }

        movie.setPoster(poster);
        movieRepository.save(movie);
        return poster;
    }

    public void deletePoster(Movie movie) throws IOException {
        MoviePoster poster = movie.getPoster();
        String posterPath = poster.getPath();
        Path path = Paths.get(posterPath);

        movie.setPoster(null);
        moviePosterRepository.delete(poster);

        Files.delete(path);
    }


    // Function to generate a unique file name (you can implement as per your requirement)
    private String generateUniqueFileName() {
        // Implement your logic to generate a unique file name here
        // For example, using timestamp or UUID
        return "poster_image_" + System.currentTimeMillis() + ".jpg";
    }


    public void addFavoriteMoviesToUser(Set<Long> movieIds) {
        User user = securityService.getCurrentLoginUser().get();

        if (user != null) {
            if (movieIds != null && !movieIds.isEmpty()) {
                Set<Movie> favoriteMovies = movieRepository.findAllById(movieIds).stream().collect(Collectors.toSet());

                if (favoriteMovies.size() == movieIds.size()) {
                    // Replace existing favorite movies with new ones
                    user.getFavoriteMovies().clear();
                    user.getFavoriteMovies().addAll(favoriteMovies);

                    userRepository.save(user);
                } else {
                    throw new IllegalArgumentException("Invalid movie IDs provided.");
                }
            } else {
                throw new IllegalArgumentException("At least one movie ID should be provided.");
            }
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }


    public Set<Movie> getFavoriteMovies() {
        User user = securityService.getCurrentLoginUser().get();

        if (user != null) {
            return user.getFavoriteMovies();
        } else {
            throw new EntityNotFoundException("User not found.");
        }
    }


    public void shareMoviesBetweenUsers(Long userId2, Set<Long> movieIds) {
        User user1 = securityService.getCurrentLoginUser().orElse(null);
        User user2 = userRepository.findById(userId2).orElse(null);

        if (user1 != null && user2 != null) {
            if (movieIds != null && movieIds.size() == 3) {
                SharedFavoriteMovies existingSharedMovies = sharedFavoriteMoviesRepository
                        .findByUsers(user1, user2)
                        .orElse(null);

                if (existingSharedMovies == null) {
                    if (movieIds != null && movieIds.size() == 3) {
                        Iterable<Movie> moviesIterable = movieRepository.findAllById(movieIds);
                        Set<Movie> moviesToShare = new HashSet<>();
                        for (Movie movie : moviesIterable) {
                            moviesToShare.add(movie);
                        }


                        if (moviesToShare.size() == 3) {
                            SharedFavoriteMovies sharedFavoriteMovies = new SharedFavoriteMovies();
                            sharedFavoriteMovies.setUser1(user1);
                            sharedFavoriteMovies.setUser2(user2);
                            sharedFavoriteMovies.getFavoriteMovies().addAll(moviesToShare);

                            sharedFavoriteMoviesRepository.save(sharedFavoriteMovies);
                        } else {
                            throw new IllegalArgumentException("Exactly three valid movie IDs should be provided.");
                        }
                    } else {
                        throw new IllegalStateException("Movies already shared between these users.");
                    }
                } else {
                    throw new IllegalArgumentException("Exactly three movie IDs should be provided.");
                }
            } else {
                throw new EntityNotFoundException("Users not found.");
            }
        }
    }
    public Set<Movie> getSharedMoviesBetweenUsers(Long userId2) {
        Long userId1 = securityService.getCurrentLoginUserId();
        return sharedFavoriteMoviesRepository.findMoviesByUserIds(userId1, userId2);
    }



}