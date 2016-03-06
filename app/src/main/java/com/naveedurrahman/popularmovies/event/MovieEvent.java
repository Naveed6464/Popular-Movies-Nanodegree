package com.naveedurrahman.popularmovies.event;


import com.naveedurrahman.popularmovies.model.Movie;

import java.util.List;

/**
 * Ths event class is used to represent response returned by discover endpoint of  Open Movie DB API.
 */
public class MovieEvent {

    List<Movie> movieList;

    public MovieEvent() {
    }

    public MovieEvent(List<Movie> movieList) {
        this.movieList = movieList;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
    }
}
