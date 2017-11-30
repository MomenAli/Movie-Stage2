package com.example.engmomenali.movieappmaster.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Movie;

/**
 * Created by Momen Ali on 11/23/2017.
 */



public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mMovieDBHelper;

    private static final int MOVIE = 990;
    private static final int MOVIE_WITH_ID = 388;


    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.AUTHORITY;

        matcher.addURI(authority, MovieEntry.TABLENAME, MOVIE);
        matcher.addURI(authority, MovieEntry.TABLENAME + "/#", MOVIE_WITH_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mMovieDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match){
            case MOVIE:
                  return db.query(MovieEntry.TABLENAME,
                          projection,
                          selection,
                          selectionArgs,
                          null,
                          null,
                          sortOrder);

            case MOVIE_WITH_ID:
                String mSelection = MovieEntry._ID+ "=?";
                String id = String.valueOf(ContentUris.parseId(uri));
                String [] mSelectionArgs = new String[]{id};
                return  db.query(MovieEntry.TABLENAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
            default:   throw new UnsupportedOperationException("Unknown uri "+ uri);
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        Uri returnUri;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                long _id = db.insert(MovieEntry.TABLENAME,null,values);
                if(_id > 0)
                    returnUri = MovieEntry.buildMovieUri(_id);
                else
                    throw new android.database.SQLException("Field to insert into " + uri);
                break;
                default:   throw new UnsupportedOperationException("Unknown uri "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int returnid;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE:
                returnid = db.delete(MovieEntry.TABLENAME, selection, selectionArgs);
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieEntry.TABLENAME + "'");

                break;
            case MOVIE_WITH_ID:
                String mSelection = MovieEntry._ID + "=?";
                String[] mSelectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                returnid = db.delete(MovieEntry.TABLENAME, mSelection, mSelectionArgs);

                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieEntry.TABLENAME + "'");
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }
        return returnid;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        long returnid;
        int count = 0;
        int match = sUriMatcher.match(uri);
        switch (match) {

            case MOVIE:
                db.beginTransaction();
                try {

                    for (ContentValues cv : values
                            ) {
                        returnid = db.insert(MovieEntry.TABLENAME, null, cv);
                        if (returnid != 0)
                            count++;

                    }
                    if (count > 0) {
                        db.setTransactionSuccessful();
                    }
                }finally {

                    db.endTransaction();
                }
                if (count>0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                Log.d("lifecycle", "bulkInsert: "+ count);
                return count;
                default:
                    return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();
        int numUpdated = 0;

        int match = sUriMatcher.match(uri);

        switch(match){
            case MOVIE:{
                numUpdated = db.update(MovieEntry.TABLENAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {
                numUpdated = db.update(MovieEntry.TABLENAME,
                        values,
                        MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                Log.d("MovieDetailsActivity", "update: favorite "+String.valueOf(ContentUris.parseId(uri)));

                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;

    }
}
