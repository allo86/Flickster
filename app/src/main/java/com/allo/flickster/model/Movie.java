package com.allo.flickster.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * Created by ALLO on 18/7/16.
 */
public class Movie implements Parcelable {

    private String title;

    private String overview;

    private String posterPath;

    private String backdropPath;

    protected Movie() {

    }

    public Movie(JSONObject jsonObject) throws JSONException {
        this();

        if (jsonObject.has("title")) setTitle(jsonObject.getString("title"));
        if (jsonObject.has("overview")) setOverview(jsonObject.getString("overview"));
        if (jsonObject.has("poster_path")) setPosterPath(jsonObject.getString("poster_path"));
        if (jsonObject.has("backdrop_path")) setBackdropPath(jsonObject.getString("backdrop_path"));
    }

    protected Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterUrl(int width) {
        return String.format(Locale.getDefault(), "https://image.tmdb.org/t/p/w%d/%s", width, getPosterPath());
    }

    public String getBackdropUrl(int width) {
        return String.format(Locale.getDefault(), "https://image.tmdb.org/t/p/w%d/%s", width, getBackdropPath());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
    }
}