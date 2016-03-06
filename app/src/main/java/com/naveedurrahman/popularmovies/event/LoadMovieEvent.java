package com.naveedurrahman.popularmovies.event;

/**
 * This event class is used to represent an event which is triggered to fetch movies from Open Movie DB API.
 */
public class LoadMovieEvent {
    private String sortBy;

    public LoadMovieEvent(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
}
