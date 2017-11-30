package com.example.engmomenali.movieappmaster.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class NetworkUtils {

    final static String BASE_URL =
            URLParameters.MOVIE_DB_SITE_URL;

    final static String primary_release_year = "primary_release_year";
    final static String year = "2010";

    final static String PARAM_SORT = "sort_by";

    final static String pop_PARAM = "/popular";
    final static String Vote_PARAM = "/top_rated";

    final static String api_key = "api_key";
    final static String key = URLParameters.API_KEY;

    public static URL buildUrl(int sort) {
        String temp;
        switch(sort){
            case 1:  temp = pop_PARAM;
                break;
            case 0: temp = Vote_PARAM;
                break;
            default:  temp = "/now_playing";
        }

        Uri builtUri = Uri.parse(BASE_URL+temp).buildUpon()
                .appendQueryParameter(api_key, key)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
    public static String buildUrl(String BaseUrl) {

        Uri builtUri = Uri.parse(BaseUrl).buildUpon()
                .appendQueryParameter(api_key, key)
                .build();
        Log.d("buildUrl", "buildUrl: "+builtUri.toString());
        return builtUri.toString();
    }
    // the same function in lesson 2

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}