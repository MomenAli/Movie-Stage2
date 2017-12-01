package com.example.engmomenali.movieappmaster.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.example.engmomenali.movieappmaster.Reviews.Review;
import com.example.engmomenali.movieappmaster.Trailers.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by MomenAli on 10/16/2017.
 */

public class MovieJsonUtils {


    public static String TAG = "SearchResults";


    public static ContentValues[] getMovies(Context context, String JsonStr) throws JSONException {
        //ArrayList<Movie> mMovieList = new ArrayList<Movie>();
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
        final String OWM_Results = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_id = "id";
        final String OWM_Title = "original_title";
        final String OWM_poster_path = "poster_path";
        final String OWM_overview = "overview";
        final String OWM_vote_average = "vote_average";
        final String OWM_release_date = "release_date";
        final String OWM_Popularity = "popularity";
        final String OWM_CoverPoster = "backdrop_path";
        Log.d("TAG", "getMovies: jsonstr ====== > " + JsonStr);
        JSONObject MoviesJson = new JSONObject(JsonStr);

                /* Is there an error? */
        if (MoviesJson.has(OWM_MESSAGE_CODE)) {
            int errorCode = MoviesJson.getInt(OWM_MESSAGE_CODE);

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


        JSONArray MovieJsonArray = MoviesJson.getJSONArray(OWM_Results);

        ContentValues[] cv = new ContentValues[MovieJsonArray.length()];
        for (int i = 0; i < MovieJsonArray.length(); i++) {


            cv[i] = new ContentValues();

            JSONObject MovieJSONItem = MovieJsonArray.getJSONObject(i);

            cv[i].put(MovieContract.MovieEntry._ID, MovieJSONItem.getLong(OWM_id));
            cv[i].put(MovieContract.MovieEntry.TITLE, MovieJSONItem.getString(OWM_Title));
            cv[i].put(MovieContract.MovieEntry.POSTERPATH, MovieJSONItem.getString(OWM_poster_path));
            cv[i].put(MovieContract.MovieEntry.RATING, MovieJSONItem.getDouble(OWM_vote_average));
            cv[i].put(MovieContract.MovieEntry.RELEASEDATE, MovieJSONItem.getString(OWM_release_date));
            cv[i].put(MovieContract.MovieEntry.OVERVIEW, MovieJSONItem.getString(OWM_overview));
            cv[i].put(MovieContract.MovieEntry.POPULARITY, MovieJSONItem.getDouble(OWM_Popularity));
            cv[i].put(MovieContract.MovieEntry.COVERIMAGEPATH, MovieJSONItem.getString(OWM_CoverPoster));


        }
        return cv;
    }

    public static Trailer[] getTrailers(Context context, String JsonStr) throws JSONException {


        //definre the parameter of the page
        final String OWM_Results = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_id = "id";
        final String OWM_key = "key";
        final String OWM_size = "size";

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
        JSONArray trailerJsonArray = trailerJson.getJSONArray(OWM_Results);

        Trailer[] trailers = new Trailer[trailerJsonArray.length()];
        for (int i = 0; i < trailerJsonArray.length(); i++) {

            trailers[i] = new Trailer();
            JSONObject trailerJSONItem = trailerJsonArray.getJSONObject(i);
            trailers[i].setId(trailerJSONItem.getString(OWM_id));
            trailers[i].setKey(trailerJSONItem.getString(OWM_key));
            trailers[i].setSize(trailerJSONItem.getString(OWM_size));

        }
        return trailers;
    }

    public static Review[] getReviews(Context context, String JsonStr) throws JSONException {


        //definre the parameter of the page
        final String OWM_Results = "results";
        final String OWM_MESSAGE_CODE = "cod";

        final String OWM_id = "id";
        final String OWM_author = "author";
        final String OWM_Content = "content";
        final String OWM_url = "url";
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
        JSONArray reviewJsonArray = reviewJson.getJSONArray(OWM_Results);

        Review[] reviews = new Review[reviewJsonArray.length()];
        for (int i = 0; i < reviewJsonArray.length(); i++) {
            reviews[i] = new Review();

            JSONObject reviewJSONItem = reviewJsonArray.getJSONObject(i);

            reviews[i].setId(reviewJSONItem.getString(OWM_id));

            reviews[i].setContent(reviewJSONItem.getString(OWM_Content));

            reviews[i].setAuthor(reviewJSONItem.getString(OWM_author));

            reviews[i].setUrl(reviewJSONItem.getString(OWM_url));

        }
        return reviews;
    }

}
