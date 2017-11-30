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

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.example.engmomenali.movieappmaster.MianMovieFragment;
import com.example.engmomenali.movieappmaster.NetworkUtils;
import com.example.engmomenali.movieappmaster.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Movie;

/**
 * Created by Momen Ali on 11/26/2017.
 */

public class MovieSyncTask {

    private static String  TAG_life = "lifecycle";
    synchronized public static void syncMovie(Context context) {
        Log.d("TAG", "syncMovie: i did quary *************************************");
        URL url = NetworkUtils.buildUrl(MianMovieFragment.Search_Sort);

        try {
            String jsonResult = NetworkUtils.getResponseFromHttpUrl(url);

            ContentValues[] contentValues = MovieJsonUtils.getMovies(context,jsonResult);

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
            ContentValues cvTemp = new ContentValues();
            for (int i = 0; i < updatedCount;i++){
                cvTemp.put(MovieEntry.RATING,cvUpdate[i].getAsString(MovieEntry.RATING));
                cvTemp.put(MovieEntry.POPULARITY,cvUpdate[i].getAsString(MovieEntry.POPULARITY));
                cvTemp.put(MovieEntry.OVERVIEW,cvUpdate[i].getAsString(MovieEntry.OVERVIEW));
                contentResolver.update(MovieEntry.buildMovieUri(cvUpdate[i].getAsLong(MovieEntry._ID)),cvTemp,null,null);
            }
            Log.d(TAG_life, "syncMovie: insertedCount  bulkInsert " +insertedCount );

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
                    MovieContract.MovieEntry.RELEASEDATE + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RELEASEDATE)) + " " +
                    MovieContract.MovieEntry.Favorit + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.Favorit));
            Log.d(TAG_life, "Movie: "+ss);
        }
    }
}
