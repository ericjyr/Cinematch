package com.cm.cinematchapp.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The `MovieEntity` class defines an Entity that is used to store information about movies in the database.
 *
 * @author Mateus Souza
 */
@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="movie_id")
    private Long id;

    @Column
    private String title;

    @Column
    private Integer year;

    @Column
    private String description;

    @Column
    private String rated;

//    @Column
//    private String director;


    @Embedded
    private StreamingInfo streamingInfo;

//    private List<Genre> genres


    @OneToOne
    @JoinColumn(name = "poster_id") // Assuming you have a foreign key named avatar_id
    private MoviePoster poster;

}


