package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
/**
 * The `MovieRepository` interface is responsible for managing movie posters in the database.
 * It extends the JpaRepository interface.
 *
 * @author Mateus Souza
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    /**
     * Retrieve a movie by its ID on the database.
     *
     * @param movieId The ID of the movie in the database.
     * @return The Movie entity if found, or null if not found.
     */
    Movie getMovieById(Long movieId);

    @Query("SELECT CASE WHEN COUNT(m.poster) > 0 THEN true ELSE false END FROM Movie m WHERE m.id = :movieId")
    boolean posterExistByMovieId(@Param("movieId") Long movieId);
}
