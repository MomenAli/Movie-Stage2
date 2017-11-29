package com.example.engmomenali.movieappmaster.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

import com.example.engmomenali.movieappmaster.MianMovieFragment;
import com.example.engmomenali.movieappmaster.NetworkUtils;
import com.example.engmomenali.movieappmaster.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Movie;

/**
 * Created by Momen Ali on 11/26/2017.
 */

public class MovieSyncTask {
    synchronized public static void syncMovie(Context context) {
        Log.d("TAG", "syncMovie: i did quary *************************************");
        URL url = NetworkUtils.buildUrl(MianMovieFragment.Search_Sort);

        try {
            String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

            ContentValues[] contentValues = MovieJsonUtils.getMovies(context,jsonResult);

            if (contentValues == null || contentValues.length == 0)return;

            ContentResolver contentResolver = context.getContentResolver();


            contentResolver.delete(MovieEntry.CONTENT_URI,null,null);
            contentResolver.bulkInsert(MovieEntry.CONTENT_URI, contentValues);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
