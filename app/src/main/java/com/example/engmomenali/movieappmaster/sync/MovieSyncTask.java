package com.example.engmomenali.movieappmaster.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.example.engmomenali.movieappmaster.MianMovieFragment;
import com.example.engmomenali.movieappmaster.Movie;
import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.Utils.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Data.MovieContract.*;

/**
 * Created by Momen Ali on 11/26/2017.
 */

public class MovieSyncTask {

    private static String  TAG_life = "lifecycle";
    synchronized public static void syncMovie(Context context) {
        Log.d("TAG", "syncMovie: i did quary *************************************");
        URL url = NetworkUtils.buildUrl(2);

        try {
            String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

            ArrayList<Movie> Movies = MovieJsonUtils.getMovies(context,jsonResult);

            ContentValues[] contentValues = new ContentValues[Movies.size()];
            for(int i = 0 ; i < Movies.size(); i++){
                Movie movie = Movies.get(i);
                contentValues[i].put(MovieEntry.POSTERPATH,movie.getPosterPath());
                contentValues[i].put(MovieEntry._ID,movie.getId());
                contentValues[i].put(MovieEntry.OVERVIEW,movie.getoverview());
                contentValues[i].put(MovieEntry.RATING,movie.getRatings());
                contentValues[i].put(MovieEntry.POPULARITY,movie.getPopularity());
                contentValues[i].put(MovieEntry.TITLE,movie.getTitle());
                contentValues[i].put(MovieEntry.RELEASEDATE,movie.getReleaseDate());
                contentValues[i].put(MovieEntry.COVERIMAGEPATH,movie.getCoverImagePath());
            }
            if (contentValues == null || contentValues.length == 0)return;
            int [] insert_index = new int[contentValues.length];
            Arrays.fill(insert_index, -1);
            ContentResolver contentResolver = context.getContentResolver();
            String mSortOrder = MovieEntry._ID;

            Cursor mCursor = contentResolver.query(MovieEntry.CONTENT_URI,null,null,null,mSortOrder);
            PrintCursor(mCursor);
            if(mCursor.getCount() == 0){
                InsertMovies(context);
                return;
            }
            int [] update_index = new int[mCursor.getCount()];
            Arrays.fill(update_index, -1);
            int j = 0;
            int insertedCount= 0 ;
            int updatedCount= 0 ;

            // check if the movie aleardy exist
            for (ContentValues cv:contentValues
                 ) {
                Log.d(TAG_life, "syncMovie: cv id " + cv.getAsString(MovieEntry._ID));
                for (int i = mCursor.getCount()-1 ; i>=0;i--){
                    mCursor.moveToPosition(i);
                    Log.d(TAG_life, "syncMovie: cursor id " + mCursor.getString(mCursor.getColumnIndex(MovieEntry._ID)) + " "+mCursor.getString(mCursor.getColumnIndex(MovieEntry.TITLE)));

                    if (Integer.parseInt(mCursor.getString(mCursor.getColumnIndex(MovieEntry._ID)))==Integer.parseInt(cv.getAsString(MovieEntry._ID))){
                        update_index[j] = j;
                        updatedCount++;
                        Log.d(TAG_life, "syncMovie: updated count up "  );
                        break;
                    }
                }
                // if the movie not exixt but it in the inserted list
                if (update_index[j] != j){
                    insert_index[j] = j;
                    insertedCount++;
                    Log.d(TAG_life, "syncMovie: inserted count up " + insertedCount  );
                }
                j++;
            }


            j = 0;
            Log.d(TAG_life, "syncMovie: "+ insertedCount + "  up "+updatedCount);
            ContentValues[] cvInsert = new ContentValues[insertedCount];
            ContentValues[] cvUpdate = new ContentValues[updatedCount];
            insertedCount = 0;
            updatedCount  = 0;

            //splite the inserted movie and the update movie
            for (ContentValues cv:contentValues
                    ) {

                Log.d(TAG_life, "syncMovie:"+j+" //////////////////insert_index" + insert_index[j]);
                Log.d(TAG_life, "syncMovie: update_index" + update_index[j]);
                if (insert_index[j]==j){
                    cvInsert[insertedCount++] = cv;
                    Log.d(TAG_life, "syncMovie: insert number " + insertedCount);
                }

                if (update_index[j]==j){
                    cvUpdate[updatedCount++] = cv;
                }
                j++;
            }
            // update the movie
            ContentValues cvTemp = new ContentValues();
            for (int i = 0; i < updatedCount;i++){
                cvTemp.put(MovieEntry.RATING,cvUpdate[i].getAsString(MovieEntry.RATING));
                cvTemp.put(MovieEntry.POPULARITY,cvUpdate[i].getAsString(MovieEntry.POPULARITY));
                cvTemp.put(MovieEntry.OVERVIEW,cvUpdate[i].getAsString(MovieEntry.OVERVIEW));
                cvTemp.put(MovieEntry.POSTERPATH,cvUpdate[i].getAsString(MovieEntry.POSTERPATH));
                contentResolver.update(MovieEntry.buildMovieUri(cvUpdate[i].getAsLong(MovieEntry._ID)),cvTemp,null,null);
            }
            Log.d(TAG_life, "syncMovie: insertedCount  bulkInsert " +insertedCount );

            // insert the new movie
            if (cvInsert.length>0)
            contentResolver.bulkInsert(MovieEntry.CONTENT_URI, cvInsert);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    synchronized public static void InsertMovies(Context context) {
        Log.d("TAG", "syncMovie: i did quary *************************************");
        URL url = NetworkUtils.buildUrl(2);

        try {
            String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

            ArrayList<Movie> Movies = MovieJsonUtils.getMovies(context,jsonResult);

            ContentValues[] contentValues = new ContentValues[Movies.size()];
            for(int i = 0 ; i < Movies.size(); i++){
                Movie movie = Movies.get(i);
                contentValues[i].put(MovieEntry.POSTERPATH,movie.getPosterPath());
                contentValues[i].put(MovieEntry._ID,movie.getId());
                contentValues[i].put(MovieEntry.OVERVIEW,movie.getoverview());
                contentValues[i].put(MovieEntry.RATING,movie.getRatings());
                contentValues[i].put(MovieEntry.POPULARITY,movie.getPopularity());
                contentValues[i].put(MovieEntry.TITLE,movie.getTitle());
                contentValues[i].put(MovieEntry.RELEASEDATE,movie.getReleaseDate());
                contentValues[i].put(MovieEntry.COVERIMAGEPATH,movie.getCoverImagePath());
            }

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
    public static void PrintCursor(Cursor mCursor){
        if (mCursor.getCount()==0)return;
        for(int i = 0 ; i < mCursor.getCount();i++) {
            mCursor.moveToPosition(i);
            String ss = MovieContract.MovieEntry._ID + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry._ID)) + " " +
                    MovieContract.MovieEntry.TITLE + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.TITLE)) + " " +
                    MovieContract.MovieEntry.POSTERPATH + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POSTERPATH)) + " " +
                    MovieContract.MovieEntry.OVERVIEW + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)) + " " +
                    MovieContract.MovieEntry.RATING + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RATING)) + " " +
                    MovieContract.MovieEntry.POPULARITY + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POPULARITY)) + " " +
                    MovieContract.MovieEntry.COVERIMAGEPATH + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COVERIMAGEPATH)) + " " +
                    MovieContract.MovieEntry.RELEASEDATE + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RELEASEDATE));
            Log.d(TAG_life, "Movie: "+ss);
        }
    }
}
