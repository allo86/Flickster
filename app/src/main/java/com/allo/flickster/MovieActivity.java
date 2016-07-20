package com.allo.flickster;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
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
    RatingBar mRatingBar;

    @BindView(R.id.iv_backdrop)
    ImageView mIvBackdrop;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    private YouTubePlayer YPlayer;

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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Go back
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeUI() {
        Drawable progress = mRatingBar.getProgressDrawable();
        DrawableCompat.setTint(progress, ContextCompat.getColor(this, R.color.colorPrimary));

        mFragmentContainer.setVisibility(View.INVISIBLE);
        mRatingBar.setVisibility(View.INVISIBLE);
        mIvBackdrop.setVisibility(View.INVISIBLE);
        tvDescription.setVisibility(View.INVISIBLE);
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
        if (movie.getAverageRating().compareTo(5D) >= 0D) {
            // Check if movie has videos
            MoviesRestClientImplementation.getMovieVideos(movie.getMovieId(), new MovieVideosCallback() {
                @Override
                public void onSuccess(ArrayList<Video> videos) {
                    movie.setVideos(videos);
                    showMovieVideo();
                    showMovieData();
                }

                @Override
                public void onError(Error error) {
                    showMovieData();
                }
            });
        } else {
            showMovieData();
        }
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
                        Log.d(TAG_LOG, "youtube videos: " + keys.size());
                        YPlayer.cueVideos(keys);
                        YPlayer.play();
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
        mRatingBar.setRating(movie.getAverageRating().floatValue() / 2);

        mRatingBar.setVisibility(View.VISIBLE);
        mIvBackdrop.setVisibility(View.VISIBLE);
        tvDescription.setVisibility(View.VISIBLE);
    }
}
