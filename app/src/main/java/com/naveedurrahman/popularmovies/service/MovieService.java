package com.naveedurrahman.popularmovies.service;

import android.util.Log;

import com.naveedurrahman.popularmovies.App;
import com.naveedurrahman.popularmovies.event.LoadMovieEvent;
import com.naveedurrahman.popularmovies.event.MovieEvent;
import com.naveedurrahman.popularmovies.model.Movie;
import com.naveedurrahman.popularmovies.model.MovieInfo;
import com.naveedurrahman.popularmovies.util.Constants;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MovieService {

    private Bus mEventBus;
    private List<Movie> mMovieList;

    private static final String LOG_TAG = MovieService.class.getSimpleName();

    public MovieService(Bus eventBus) {
        mEventBus = eventBus;
        eventBus.register(this);
    }

    public MovieService() {
        App.getEventBus().register(this);
    }

    /**
     * Used to make a async call to movies DB to fetch a list of popular movies.
     *
     * @param event
     */
    @Subscribe
    public void onDiscoverMovieEvent(LoadMovieEvent event) {

        Retrofit client = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IMovieService api = client.create(IMovieService.class);

        Call<MovieInfo> restCall = api.getPopularMovies(event.getSortBy(), Constants.MOVIE_DB_API_KEY);

        restCall.enqueue(new Callback<MovieInfo>() {
            @Override
            public void onResponse(Response<MovieInfo> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    MovieInfo movieInfo = response.body();
                    mMovieList = movieInfo.getmMovieList();
                    App.getEventBus().post(produceMovieEvent());
                } else {
                    //request not successful (like 400,401,403 etc)
                    //Handle errors
                    Log.d(LOG_TAG, "Web call error");
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public MovieEvent produceMovieEvent() {
        return new MovieEvent(mMovieList);
    }
}
