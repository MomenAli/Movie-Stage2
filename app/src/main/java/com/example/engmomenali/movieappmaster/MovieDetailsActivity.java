package com.example.engmomenali.movieappmaster;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    String TAG = "MovieDetailsActivity";
    public static String TagId;
    public Boolean Favorate = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView IM = (ImageView) findViewById(R.id.DetailsIV);
        TextView  TVOverView = (TextView) findViewById(R.id.DetailsOverView);
        TextView  TVUserRating = (TextView) findViewById(R.id.UserRating);
        TextView  TVReleaseDate = (TextView) findViewById(R.id.ReleaseDate);
        ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star);

            Bundle extras = getIntent().getExtras();


            setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
            TVOverView.setText(extras.getString(MovieContract.MovieEntry.OVERVIEW));
            TVUserRating.setText(extras.getString(MovieContract.MovieEntry.RATING));
            TVReleaseDate.setText( extras.getString(MovieContract.MovieEntry.RELEASEDATE));
            TagId = extras.getString(MovieContract.MovieEntry._ID);
        Log.d("MovieDetailsActivity", "onItemClick: favorite "+extras.getString(MovieContract.MovieEntry.Favorit));

        if (extras.getString(MovieContract.MovieEntry.Favorit).equals("0")){
                Favorate = false;
                favoriteStar.setImageResource(android.R.drawable.btn_star_big_off);
            }else{
                Favorate = true;
                favoriteStar.setImageResource(android.R.drawable.btn_star_big_on);
            }
            Picasso.with(this)
                    .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+extras.getString(MovieContract.MovieEntry.POSTERPATH))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(IM);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            public Cursor loadInBackground() {

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void MakeAsFavorite(View view) {
        ImageView IM = (ImageView) findViewById(R.id.favorite_star);
        if(Favorate){
            Favorate = false;
            IM.setImageResource(android.R.drawable.btn_star_big_off);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.Favorit,"0");
            getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)),contentValues,null,null);
            Log.d(TAG, "MakeAsFavorite: "+ MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)));
            Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)),null,null,null,null);
            Log.d(TAG, "MakeAsFavorite: "+PrintCursor(mCursor));

        }else {
            IM.setImageResource(android.R.drawable.btn_star_big_on);
            Favorate = true;
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieEntry.Favorit,"1");
            getContentResolver().update(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)),contentValues,null,null);
            Log.d(TAG, "MakeAsFavorite: "+ MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)));
            Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)),null,null,null,null);
            Log.d(TAG, "MakeAsFavorite: "+PrintCursor(mCursor));

        }
    }
    public String PrintCursor(Cursor mCursor){
        if (mCursor.getCount()==0)return "/////////empty";
        mCursor.moveToPosition(0);
        String ss = MovieContract.MovieEntry._ID +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry._ID)) +" "+
        MovieContract.MovieEntry.TITLE +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.TITLE)) +" "+
        MovieContract.MovieEntry.POSTERPATH +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POSTERPATH)) +" "+
        MovieContract.MovieEntry.OVERVIEW +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)) +" "+
        MovieContract.MovieEntry.RATING +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RATING)) +" "+
        MovieContract.MovieEntry.POPULARITY +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POPULARITY)) +" "+
        MovieContract.MovieEntry.COVERIMAGEPATH +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COVERIMAGEPATH))+" "+
        MovieContract.MovieEntry.RELEASEDATE +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RELEASEDATE)) +" "+
        MovieContract.MovieEntry.Favorit +" "+mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.Favorit));
        return ss;
    }
}
