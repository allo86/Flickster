package com.allo.flickster;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allo.flickster.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Movies Adapter
 * <p/>
 * Created by ALLO on 18/7/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static final String TAG_LOG = MoviesAdapter.class.getCanonicalName();

    public interface OnMoviesAdapterListener {
        void didSelectMovie(Movie movie);
    }

    private OnMoviesAdapterListener mListener;

    private ArrayList<Movie> mMovies;

    public MoviesAdapter(ArrayList<Movie> movies, OnMoviesAdapterListener listener) {
        this.mMovies = movies;
        this.mListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.configureViewWithMovie(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return this.mMovies != null ? this.mMovies.size() : 0;
    }

    public void notifyDataSetChanged(ArrayList<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private View view;

        private Movie movie;

        @Nullable
        @BindView(R.id.iv_poster)
        ImageView ivPoster;

        @Nullable
        @BindView(R.id.iv_backdrop)
        ImageView ivBackdrop;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_overview)
        TextView tvOverview;

        public MovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.view = view;
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.didSelectMovie(movie);
                }
            });

            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            if (ivPoster != null) {
                Log.d(TAG_LOG, "Portrait");
                int width = (int) (metrics.widthPixels * 0.5);
                if (width > 780) {
                    width = 780;
                } else if (width > 500) {
                    width = 500;
                } else if (width > 342) {
                    width = 342;
                } else if (width > 185) {
                    width = 185;
                } else {
                    width = 154;
                }
                ivPoster.setLayoutParams(new RelativeLayout.LayoutParams(width, (int) (width * 1.5)));
            } else if (ivBackdrop != null) {
                Log.d(TAG_LOG, "Landscape");
                int width = (int) (metrics.widthPixels * 0.75);
                if (width > 1280) {
                    width = 1280;
                } else if (width > 780) {
                    width = 780;
                } else {
                    width = 300;
                }
                ivBackdrop.setLayoutParams(new RelativeLayout.LayoutParams(width, (int) (width * 0.56282)));
            }
        }

        public void configureViewWithMovie(Movie movie) {
            this.movie = movie;

            this.tvTitle.setText(movie.getTitle());
            this.tvOverview.setText(movie.getOverview());
            if (ivPoster != null) {
                ivPoster.setImageDrawable(null);
                Picasso.with(view.getContext()).load(movie.getPosterUrl(ivPoster.getMeasuredWidth())).into(ivPoster);
            }
            if (ivBackdrop != null) {
                ivBackdrop.setImageDrawable(null);
                Picasso.with(view.getContext()).load(movie.getBackdropUrl(ivBackdrop.getMeasuredWidth())).into(ivBackdrop);
            }
        }
    }

}
