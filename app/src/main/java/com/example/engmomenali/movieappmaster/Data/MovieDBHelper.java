package com.example.engmomenali.movieappmaster.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.engmomenali.movieappmaster.Data.MovieContract.*;

/**
 * Created by Momen Ali on 11/23/2017.
 */

public class MovieDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 6;
    private static final String DB_NAME = "movieapp.db";

    public MovieDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String Query_Create_Table = "CREATE TABLE " + MovieEntry.TABLENAME +
                "(" + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
                MovieEntry.TITLE + " TEXT NOT NULL," +
                MovieEntry.RELEASEDATE + " DATE NOT NULL , " +
                MovieEntry.POPULARITY + " REAL NOT NULL ," +
                MovieEntry.RATING + " REAL NOT NULL ," +
                MovieEntry.OVERVIEW + " TEXT NOT NULL," +
                MovieEntry.COVERIMAGEPATH + " TEXT NOT NULL," +
                MovieEntry.POSTERPATH + " TEXT NOT NULL," +
                MovieEntry.Favorit + " INTEGER DEFAULT 0 " +
                ");";
        db.execSQL(Query_Create_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLENAME);
        onCreate(db);
    }
}
