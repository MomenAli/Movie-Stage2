package com.example.engmomenali.movieappmaster.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

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
            int [] insert_index = new int[contentValues.length];
            Arrays.fill(insert_index, 0);
            ContentResolver contentResolver = context.getContentResolver();
            String mSortOrder = MovieEntry._ID;

            Cursor mCursor = contentResolver.query(MovieEntry.CONTENT_URI,null,null,null,mSortOrder);
            int [] update_index = new int[mCursor.getCount()];
            Arrays.fill(update_index, 0);
            int j = 0;
            int insertedCount= 0 ;
            int updatedCount= 0 ;

            for (ContentValues cv:contentValues
                 ) {

                for (int i = mCursor.getCount() ; i>=0;i--){
                    if(Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MovieEntry._ID)))<Integer.parseInt(cv.getAsString(MovieEntry._ID))){
                       insert_index[j] = j;
                        insertedCount++;
                       break;
                    }
                    if (Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MovieEntry._ID)))==Integer.parseInt(cv.getAsString(MovieEntry._ID))){
                        update_index[j] = j;
                        updatedCount++;
                        break;
                    }
                }
                if (update_index[j] != j){
                    insert_index[j] = j;
                    insertedCount++;
                }
                j++;
            }
            j = 0;
            ContentValues[] cvInsert = new ContentValues[insertedCount];
            ContentValues[] cvUpdate = new ContentValues[updatedCount];
            insertedCount = 0;
            updatedCount  = 0;
            for (ContentValues cv:contentValues
                    ) {
                if (insert_index[j]==j){
                    cvInsert[insertedCount++] = cv;
                }

                if (update_index[j]==j){
                    cvUpdate[updatedCount++] = cv;
                }
                j++;
            }

            contentResolver.bulkInsert(MovieEntry.CONTENT_URI, contentValues);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    synchronized public static void InsertMovies(Context context) {
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
