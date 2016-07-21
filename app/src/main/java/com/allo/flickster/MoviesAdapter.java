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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allo.flickster.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Movies Adapter
 * <p/>
 * Created by ALLO on 18/7/16.
 */
public class MoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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

    enum RowType {
        REGULAR,
        POPULAR
    }

    @Override
    public int getItemViewType(int position) {
        Movie movie = mMovies.get(position);
        if (movie.getAverageRating().compareTo(5D) >= 0) {
            return RowType.POPULAR.ordinal();
        } else {
            return RowType.REGULAR.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == RowType.POPULAR.ordinal()) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_popular, parent, false);
            return new PopularMovieViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movies_regular, parent, false);
            return new MovieViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            ((MovieViewHolder) holder).configureViewWithMovie(mMovies.get(position));
        } else if (holder instanceof PopularMovieViewHolder) {
            ((PopularMovieViewHolder) holder).configureViewWithMovie(mMovies.get(position));
        }
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

        @BindView(R.id.pb_image)
        ProgressBar pbImage;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_overview)
        TextView tvOverview;

        private int width;

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
                width = (int) (metrics.widthPixels * 0.5);
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
                width = (int) (metrics.widthPixels * 0.75);
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

            this.pbImage.setVisibility(View.VISIBLE);

            if (ivPoster != null) {
                ivPoster.setImageDrawable(null);
                Picasso.with(view.getContext()).load(movie.getPosterUrl(width)).fit()
                        .into(ivPoster, new Callback() {
                            @Override
                            public void onSuccess() {
                                pbImage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
            if (ivBackdrop != null) {
                ivBackdrop.setImageDrawable(null);
                Picasso.with(view.getContext()).load(movie.getBackdropUrl(width)).fit()
                        .into(ivBackdrop, new Callback() {
                            @Override
                            public void onSuccess() {
                                pbImage.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }
    }

    class PopularMovieViewHolder extends RecyclerView.ViewHolder {
        private View view;

        private Movie movie;

        @BindView(R.id.iv_backdrop)
        ImageView ivBackdrop;

        @BindView(R.id.pb_image)
        ProgressBar pbImage;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        public PopularMovieViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

            this.view = view;
            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) mListener.didSelectMovie(movie);
                }
            });

            ivBackdrop.post(new Runnable() {
                @Override
                public void run() {
                    int width = ivBackdrop.getMeasuredWidth();
                    int height = (int) (width * 0.56282);
                    ivBackdrop.setLayoutParams(new RelativeLayout.LayoutParams(width, height));
                }
            });
        }

        public void configureViewWithMovie(Movie movie) {
            this.movie = movie;

            this.tvTitle.setText(movie.getTitle());

            this.pbImage.setVisibility(View.VISIBLE);
            ivBackdrop.setImageDrawable(null);
            Picasso.with(view.getContext()).load(movie.getBackdropUrl(1280))
                    .into(ivBackdrop, new Callback() {
                        @Override
                        public void onSuccess() {
                            pbImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }
    }

}
