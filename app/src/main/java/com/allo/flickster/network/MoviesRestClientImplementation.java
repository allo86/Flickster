package com.allo.flickster.network;

import android.util.Log;

import com.allo.flickster.model.Movie;
import com.allo.flickster.network.callback.LatestMoviesCallback;
import com.allo.flickster.network.model.Error;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ALLO on 18/7/16.
 */
public class MoviesRestClientImplementation {

    private static final String TAG_LOG = MoviesRestClientImplementation.class.getCanonicalName();

    public static void getLastestMovies(int page, final LatestMoviesCallback callback) {
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(page));

        MoviesRestClient.get("movie/now_playing", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    int page = response.getInt("page");

                    JSONArray jsonMovies = response.getJSONArray("results");
                    ArrayList<Movie> movies = new ArrayList<>();
                    for (int i = 0; i < jsonMovies.length(); i++) {
                        movies.add(new Movie(jsonMovies.getJSONObject(i)));
                    }

                    callback.onSuccess(movies, page);
                } catch (JSONException ex) {
                    Log.e(TAG_LOG, ex.toString());
                    callback.onError(new Error(ex.getMessage()));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                callback.onError(new Error(throwable.getMessage()));
            }
        });
    }

}
