package com.cm.cinematchapp.entities;

import java.util.List;

/**
 * The `MovieResult` class defines an Entity that is used to store information about the results
 * from the swiping function in the database.
 *
 * @author Mateus Souza
 */
public class MovieResult {
    private List<Movie> result;

    public List<Movie> getResult() {
        return result;
    }

    public void setResult(List<Movie> result) {
        this.result = result;
    }
}

