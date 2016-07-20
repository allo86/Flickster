package com.allo.flickster.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by ALLO on 18/7/16.
 */
public class MoviesRestClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final String MOVIES_API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (params == null) {
            params = new RequestParams();
        }
        params.put("api_key", MOVIES_API_KEY);

        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        if (params == null) {
            params = new RequestParams();
        }
        params.put("api_key", MOVIES_API_KEY);

        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
