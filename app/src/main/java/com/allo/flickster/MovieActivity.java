package com.allo.flickster;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.allo.flickster.base.BaseActivity;
import com.allo.flickster.model.Movie;
import com.allo.flickster.model.Video;
import com.allo.flickster.network.MoviesRestClientImplementation;
import com.allo.flickster.network.callback.MovieVideosCallback;
import com.allo.flickster.network.model.Error;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;

import butterknife.BindView;
import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;
import icepick.Icepick;

/**
 * Movie Details
 * Bonus: Expose details of movie (ratings using RatingBar, popularity, and synopsis) in a separate activity. (3 points)
 */
public class MovieActivity extends BaseActivity {

    public static final String BUNDLE_MOVIE = "BUNDLE_MOVIE";

    @BindView(R.id.fragment_container)
    FrameLayout mFragmentContainer;

    @BindView(R.id.rating_bar)
    AppCompatRatingBar mRatingBar;

    @BindView(R.id.iv_backdrop)
    ImageView mIvBackdrop;

    @BindView(R.id.tv_vote_count)
    TextView tvVoteCount;

    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    private YouTubePlayer YPlayer;

    private ACProgressFlower mLoadingDialog;

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        Bundle data = getIntent().getExtras();
        movie = data.getParcelable(BUNDLE_MOVIE);

        initializeUI();

        Icepick.restoreInstanceState(this, savedInstanceState);

        showData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Go back
                onBackPressed();
                return true;
            case R.id.action_share:
                share();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeUI() {
        mFragmentContainer.setVisibility(View.INVISIBLE);
        mRatingBar.setVisibility(View.INVISIBLE);
        mIvBackdrop.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);

        mLoadingDialog = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)
                .text(getString(R.string.loading))
                .fadeColor(Color.DKGRAY).build();
    }

    private void showData() {
        // Show title
        setTitle(movie.getTitle());

        // Check if need to show video
        checkIfVideo();
    }

    private void loadMovieDetails() {

    }

    private void checkIfVideo() {
        showLoadingDialog();

        // Check if movie has videos
        MoviesRestClientImplementation.getMovieVideos(movie.getMovieId(), new MovieVideosCallback() {
            @Override
            public void onSuccess(ArrayList<Video> videos) {
                movie.setVideos(videos);
                showMovieVideo();
                showMovieData();

                hideLoadingDialog();
            }

            @Override
            public void onError(Error error) {
                showMovieData();

                hideLoadingDialog();
            }
        });
    }

    private void showMovieVideo() {
        if (movie.hasYouTubeVideos()) {
            YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            youTubePlayerFragment.initialize("AIzaSyAsG6oMoSX4w-IhjaeVrBxIieniRVdzQqI", new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
                    if (!wasRestored) {
                        YPlayer = player;
                        ArrayList<String> keys = new ArrayList<>();
                        for (Video video : movie.getVideos()) {
                            if ("YouTube".equals(video.getSite())) keys.add(video.getKey());
                        }
                        if (movie.getAverageRating().compareTo(5D) > 0) {
                            // Play videos automatically
                            YPlayer.loadVideos(keys);
                        } else {
                            // Load videos but do not play automatically
                            YPlayer.cueVideos(keys);
                        }
                    }
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {

                }
            });
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fragment_container, youTubePlayerFragment).commit();
            mFragmentContainer.setVisibility(View.VISIBLE);
        }
    }

    private void showMovieData() {
        tvDescription.setText(movie.getOverview());
        tvVoteCount.setText(getString(R.string.vote_count, String.valueOf(movie.getVoteCount())));
        tvReleaseDate.setText(getString(R.string.release_date, movie.getDate()));

        try {
            LayerDrawable stars = (LayerDrawable) mRatingBar.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(ContextCompat.getColor(mRatingBar.getContext(), R.color.yellow), PorterDuff.Mode.SRC_ATOP);
            //stars.getDrawable(1).setColorFilter(ContextCompat.getColor(mRatingBar.getContext(), R.color.ultra_light_gray), PorterDuff.Mode.SRC_ATOP);
            //stars.getDrawable(0).setColorFilter(ContextCompat.getColor(mRatingBar.getContext(), R.color.ultra_light_gray), PorterDuff.Mode.SRC_ATOP);
        } catch (Exception ex) {
            // TODO: Do something here?
        }
        mRatingBar.setRating(movie.getAverageRating().floatValue() / 2);
        mRatingBar.setVisibility(View.VISIBLE);

        mIvBackdrop.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.VISIBLE);
    }

    private void share() {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
        share.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/movie/" + String.valueOf(movie.getMovieId()));
        startActivity(Intent.createChooser(share, "Share this movie!"));
    }

    private void showLoadingDialog() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
