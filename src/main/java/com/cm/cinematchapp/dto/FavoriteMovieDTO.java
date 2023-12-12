package com.cm.cinematchapp.dto;
import lombok.Data;

import java.util.Set;

@Data
public class FavoriteMovieDTO {
    private Set<Long> movieIds;
}
