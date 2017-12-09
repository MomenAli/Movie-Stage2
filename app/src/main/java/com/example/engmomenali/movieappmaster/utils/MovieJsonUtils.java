package com.example.engmomenali.movieappmaster.utils;

import android.content.Context;
import android.util.Log;

import com.example.engmomenali.movieappmaster.Movie;
import com.example.engmomenali.movieappmaster.reviews.Review;
import com.example.engmomenali.movieappmaster.trailers.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by MomenAli on 10/16/2017.
 */

public class MovieJsonUtils {


    public static String TAG = "SearchResults";


    public static ArrayList getMovies(Context context, String JsonStr) throws JSONException {
        ArrayList<Movie> mMovieList = new ArrayList<Movie>();
        /*
        *   I Need this information for this stage (Stage 1)
        *   original title
            movie poster image thumbnail
            A plot synopsis (called overview in the api)
            user rating (called vote_average in the api)
            release date
        *
        * */


        //definre the parameter of the page
        final String OWM_RESULTS = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_ID = "id";
        final String OWM_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_POPULARITY = "popularity";
        final String OWM_COVER_POSTER = "backdrop_path";
        Log.d("TAG", "getMovies: jsonstr ====== > " + JsonStr);
        JSONObject moviesJson = new JSONObject(JsonStr);

                /* Is there an error? */
        if (moviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }


        JSONArray MovieJsonArray = moviesJson.getJSONArray(OWM_RESULTS);

        for (int i = 0; i < MovieJsonArray.length(); i++) {


            Movie MovieTemp = new Movie();

            JSONObject MovieJSONItem = MovieJsonArray.getJSONObject(i);

            MovieTemp.setId(MovieJSONItem.getLong(OWM_ID));
            MovieTemp.setTitle(MovieJSONItem.getString(OWM_TITLE));
            MovieTemp.setPosterPath(MovieJSONItem.getString(OWM_POSTER_PATH));
            MovieTemp.setRatings(MovieJSONItem.getDouble(OWM_VOTE_AVERAGE));
            MovieTemp.setReleaseDate(MovieJSONItem.getString(OWM_RELEASE_DATE));
            MovieTemp.setoverview(MovieJSONItem.getString(OWM_OVERVIEW));
            MovieTemp.setPopularity(MovieJSONItem.getDouble(OWM_POPULARITY));
            MovieTemp.setCoverImagePath(MovieJSONItem.getString(OWM_COVER_POSTER));
            mMovieList.add(MovieTemp);

        }
        return mMovieList;
    }

    public static Trailer[] getTrailers(Context context, String JsonStr) throws JSONException {


        //definre the parameter of the page
        final String OWM_RESULTS = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_ID = "id";
        final String OWM_KEY = "key";
        final String OWM_SIZE = "size";

        Log.d(TAG, "getMovies: jsonstr ====== > " + JsonStr);
        JSONObject trailerJson = new JSONObject(JsonStr);

                /* Is there an error? */
        if (trailerJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = trailerJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        JSONArray trailerJsonArray = trailerJson.getJSONArray(OWM_RESULTS);

        Trailer[] trailers = new Trailer[trailerJsonArray.length()];
        for (int i = 0; i < trailerJsonArray.length(); i++) {

            trailers[i] = new Trailer();
            JSONObject trailerJSONItem = trailerJsonArray.getJSONObject(i);
            trailers[i].setId(trailerJSONItem.getString(OWM_ID));
            trailers[i].setKey(trailerJSONItem.getString(OWM_KEY));
            trailers[i].setSize(trailerJSONItem.getString(OWM_SIZE));

        }
        return trailers;
    }

    public static Review[] getReviews(Context context, String JsonStr) throws JSONException {


        //definre the parameter of the page
        final String OWM_RESULTS = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_ID = "id";
        final String OWM_AUTHOR = "author";
        final String OWM_CONTENT = "content";
        final String OWM_URL = "url";
        Log.d(TAG, "getReviews: jsonstr ====== > " + JsonStr);
        JSONObject reviewJson = new JSONObject(JsonStr);

                /* Is there an error? */
        if (reviewJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = reviewJson.getInt(OWM_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }
        JSONArray reviewJsonArray = reviewJson.getJSONArray(OWM_RESULTS);

        Review[] reviews = new Review[reviewJsonArray.length()];
        for (int i = 0; i < reviewJsonArray.length(); i++) {
            reviews[i] = new Review();

            JSONObject reviewJSONItem = reviewJsonArray.getJSONObject(i);

            reviews[i].setId(reviewJSONItem.getString(OWM_ID));

            reviews[i].setContent(reviewJSONItem.getString(OWM_CONTENT));

            reviews[i].setAuthor(reviewJSONItem.getString(OWM_AUTHOR));

            reviews[i].setUrl(reviewJSONItem.getString(OWM_URL));

        }
        return reviews;
    }

}
