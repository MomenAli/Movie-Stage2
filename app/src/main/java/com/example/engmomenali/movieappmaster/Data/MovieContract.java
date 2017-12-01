package com.example.engmomenali.movieappmaster.Data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Momen Ali on 11/23/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.engmomenali.movieappmaster";

    public static final Uri BASE_CONTENT_URL = Uri.parse("content://" + AUTHORITY);


    public static final class MovieEntry implements BaseColumns {

        public static final String TABLENAME = "movietable";

        public static final String _ID = "_id";
        public static final String TITLE = "title";
        public static final String OVERVIEW = "overview";
        public static final String RELEASEDATE = "releaseDate";
        public static final String POSTERPATH = "posterPath";
        public static final String RATING = "Rating";
        public static final String POPULARITY = "popularity";
        public static final String Favorit = "Favorit";
        public static final String COVERIMAGEPATH = "coverImagePath";


        public static final Uri CONTENT_URI = BASE_CONTENT_URL.buildUpon().appendPath(TABLENAME).build();

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
