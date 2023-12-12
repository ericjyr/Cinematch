package com.cm.cinematchapp.repositories;

import com.cm.cinematchapp.entities.Movie;
import com.cm.cinematchapp.entities.SharedFavoriteMovies;
import com.cm.cinematchapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface SharedFavoriteMoviesRepository extends JpaRepository<SharedFavoriteMovies, Long> {

    @Query("SELECT sfm FROM SharedFavoriteMovies sfm " +
            "WHERE (sfm.user1 = :user1 AND sfm.user2 = :user2) OR (sfm.user1 = :user2 AND sfm.user2 = :user1)")
    Optional<SharedFavoriteMovies> findByUsers(@Param("user1") User user1, @Param("user2") User user2);

    @Query("SELECT s.favoriteMovies FROM SharedFavoriteMovies s " +
            "WHERE (s.user1.id = :variable1 AND s.user2.id = :variable2) " +
            "OR (s.user1.id = :variable2 AND s.user2.id = :variable1)")
    Set<Movie> findMoviesByUserIds(@Param("variable1") Long userId1, @Param("variable2") Long userId2);
}
