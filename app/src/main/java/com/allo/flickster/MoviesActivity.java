package com.allo.flickster;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.allo.flickster.base.BaseActivity;
import com.allo.flickster.model.Movie;
import com.allo.flickster.network.MoviesRestClientImplementation;
import com.allo.flickster.network.callback.LatestMoviesCallback;
import com.allo.flickster.network.model.Error;
import com.allo.flickster.utils.ui.DividerItemDecoration;
import com.allo.flickster.utils.ui.EndlessRecyclerViewScrollListener;
import com.allo.flickster.utils.ui.LinearLayoutManager;

import java.util.ArrayList;

import butterknife.BindView;
import icepick.Icepick;
import icepick.State;

public class MoviesActivity extends BaseActivity implements MoviesAdapter.OnMoviesAdapterListener {

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout mSwipeToRefresh;

    @State
    ArrayList<Movie> mMovies;

    @State
    int page = 1;

    private MoviesAdapter mAdapter;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initializeUI();

        Icepick.restoreInstanceState(this, savedInstanceState);

        showData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    private void initializeUI() {
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, null, false, true));

        mAdapter = new MoviesAdapter(new ArrayList<Movie>(), this);
        mRecyclerView.setAdapter(mAdapter);

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                Log.d(TAG_LOG, "page: " + page);
                loadMovies(page);
            }
        };

        mRecyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadMovies(1);
            }
        });

    }

    private void showData() {
        if (mMovies != null) {
            // There were previous results
            endlessRecyclerViewScrollListener.setCurrentPage(page);
            mAdapter.notifyDataSetChanged(mMovies);
        } else {
            // Load the first page
            loadMovies(1);
        }
    }

    private void loadMovies(int page) {
        MoviesRestClientImplementation.getLastestMovies(page, new LatestMoviesCallback() {
            @Override
            public void onSuccess(ArrayList<Movie> movies, int page) {
                if (page == 1) {
                    MoviesActivity.this.mMovies = new ArrayList<>();
                }
                MoviesActivity.this.mMovies.addAll(movies);
                MoviesActivity.this.page = page;

                mAdapter.notifyDataSetChanged(MoviesActivity.this.mMovies);

                mSwipeToRefresh.setRefreshing(false);
            }

            @Override
            public void onError(Error error) {

            }
        });
    }

    @Override
    public void didSelectMovie(Movie movie) {
        goToMovieDetails(movie);
    }

    private void goToMovieDetails(Movie movie) {

    }
}