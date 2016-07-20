package com.allo.flickster.network.callback;

import com.allo.flickster.model.Video;
import com.allo.flickster.network.model.Error;

import java.util.ArrayList;

/**
 * Created by ALLO on 19/7/16.
 */
public interface MovieVideosCallback {

    void onSuccess(ArrayList<Video> videos);

    void onError(Error error);
}
