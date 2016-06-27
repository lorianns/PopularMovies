package com.udacity.lorianns.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lorianns on 6/25/16.
 */
public class MovieEntity implements Parcelable {

    final String _IMAGE = "poster_path";
    final String _TITLE = "original_title";
    final String _RELEASE_DATE = "release_date";
    final String _OVERVIEW = "overview";
    final String _RATING = "vote_average";
    final String _IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private String imagePath;
    private String title;
    private String releaseDate;
    private String overview;
    private String rating;

    public MovieEntity(JSONObject movieJson) throws JSONException {

            imagePath = _IMAGE_BASE_URL + movieJson.getString(_IMAGE);
            title = movieJson.getString(_TITLE);
            releaseDate = movieJson.getString(_RELEASE_DATE);
            overview = movieJson.getString(_OVERVIEW);
            rating = movieJson.getString(_RATING);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
