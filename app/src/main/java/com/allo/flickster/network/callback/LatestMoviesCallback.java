package com.allo.flickster.network.callback;

import com.allo.flickster.model.Movie;
import com.allo.flickster.network.model.Error;

import java.util.ArrayList;

/**
 * Created by ALLO on 18/7/16.
 */
public interface LatestMoviesCallback {

    void onSuccess(ArrayList<Movie> movies, int page);

    void onError(Error error);

}
