package com.naveedurrahman.popularmovies.service;

import com.naveedurrahman.popularmovies.model.MovieInfo;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;


/**
 * Defines the REST API for Retrofit to access the movie DB API.
 */
public interface IMovieService {

    @GET("/3/discover/movie")
    public Call<MovieInfo> getPopularMovies(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

}
