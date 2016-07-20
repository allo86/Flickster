package com.allo.flickster.network.callback;

import com.allo.flickster.model.Movie;
import com.allo.flickster.network.model.Error;

import java.util.ArrayList;

/**
 * Created by ALLO on 19/7/16.
 */
public interface MovieDetailsCallback {

    void onSuccess(Movie movie);

    void onError(Error error);
}
