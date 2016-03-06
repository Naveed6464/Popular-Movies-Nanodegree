package com.naveedurrahman.popularmovies.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.naveedurrahman.popularmovies.App;
import com.naveedurrahman.popularmovies.R;
import com.naveedurrahman.popularmovies.adapter.MovieAdapter;
import com.naveedurrahman.popularmovies.event.LoadMovieEvent;
import com.naveedurrahman.popularmovies.event.MovieEvent;
import com.naveedurrahman.popularmovies.model.Movie;
import com.naveedurrahman.popularmovies.service.MovieService;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */

public class MovieListFragment extends Fragment implements AdapterView.OnItemClickListener {


    private MovieService mMovieService;
    private List<Movie> mMovieList;

    /**
     * This callback makes the fragment visible to the user when the containing activity is started.
     * We want to make a network request before user can  begin interacting with the user (onResume callback)
     */
    @Override
    public void onStart() {
        super.onStart();
        mMovieService = new MovieService();
        App.getEventBus().register(this);
        fetchMovies();
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getEventBus().unregister(this);
    }

    /**
     * Used to fire an event to the Bus that will fetch movie list from Open Movie DB REST back-end.
     * The sort order is retrieved from Shared Preferences
     */
    private void fetchMovies() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortBy = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_order_default)); // default popularity.desc
        App.getEventBus().post(produceDiscoverMovieEvent(sortBy));
    }

    public LoadMovieEvent produceDiscoverMovieEvent(String queryParam) {
        return new LoadMovieEvent(queryParam);
    }

    public interface ShowDetailListener{
        void onShowDetail(Movie movie);
    }

    private static final String TAG = MovieListFragment.class.getSimpleName();

    //Keys  for saving data in onSavedInstanceState()
    public static final String SAVED_DATA = "saved_data";
    public static final String SCROLL_POSITION = "scroll_position";


    public static final String SORT_BY = "sort_by";
    public static final int POPULARITY = 1;
    public static final int HIGH_RATING = 2;

    private ShowDetailListener mShowDetailListener;


    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;
    private LinearLayout mProgressContainer;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> mList;


    public static MovieListFragment getInstance() {
        return new MovieListFragment();
    }

    public MovieListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getActivity() instanceof ShowDetailListener) {
            mShowDetailListener = (ShowDetailListener) getActivity();
        }else {
            throw new ClassCastException("Activity must implements "+ShowDetailListener.class.getSimpleName()+" interface");
        }


        mList = new ArrayList<>();
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        /*inflater.inflate(R.menu.menu_movie_list_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);

        int sort_by = getCurrentSortOrder();
        if (sort_by == POPULARITY) {
            menu.findItem(R.id.action_popularity).setChecked(true);
            menu.findItem(R.id.action_high_rating).setChecked(false);
        } else {
            menu.findItem(R.id.action_popularity).setChecked(false);
            menu.findItem(R.id.action_high_rating).setChecked(true);
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*switch (item.getItemId()) {
            case R.id.action_popularity:
                setCurrentSortOrder(POPULARITY);
                reloadListData(POPULARITY);
                break;
            case R.id.action_high_rating:
                setCurrentSortOrder(HIGH_RATING);
                reloadListData(HIGH_RATING);
                break;
        }
        getActivity().invalidateOptionsMenu();
        return super.onOptionsItemSelected(item);*/
        return true;
    }

    public void setCurrentSortOrder(int sort_by){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = preferences.edit();
        if (sort_by == POPULARITY) {
            editor.putInt(SORT_BY, POPULARITY);
        }else{
            editor.putInt(SORT_BY, HIGH_RATING);
        }
        editor.commit();

    }

    public int getCurrentSortOrder(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return preferences.getInt(SORT_BY, POPULARITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.list);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        mEmptyView = (TextView) rootView.findViewById(R.id.empty);
        mProgressContainer = (LinearLayout) rootView.findViewById(R.id.progressContainer);
        mProgressContainer.setVisibility(View.GONE);

        mEmptyView.setVisibility(View.GONE);

        mAdapter = new MovieAdapter(getActivity(), mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVED_DATA, mList);
        outState.putInt(SCROLL_POSITION, mGridView.getFirstVisiblePosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_DATA)) {
            ArrayList<Movie> list = savedInstanceState.getParcelableArrayList(SAVED_DATA);
            //setupAdapter(list);
            int position = savedInstanceState.getInt(SCROLL_POSITION);
            if (position >= 0) {
                mGridView.smoothScrollToPosition(position);
            }
        } else {
            int sort_by = getCurrentSortOrder();
            //reloadListData(sort_by);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie item = mAdapter.getItem(position);
        if (mShowDetailListener != null) {
            mShowDetailListener.onShowDetail(item);
        }
    }





    @Subscribe
    public void onMovieEvent(MovieEvent movieEvent) {
        mMovieList = movieEvent.getMovieList();
        setupAdapter();
    }

    private void setupAdapter() {
        if (getActivity() == null || mAdapter == null) return;

        if (mMovieList != null) {
            mAdapter.clear();
            mAdapter.addAll(mMovieList);
            //mAdapter.setAdapter(new GalleryItemAdapter(getContext(), mMovieList));
        } else {
            //mMovieGrid.setAdapter(null);
        }

        if (mAdapter.getCount() <= 0) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

}
