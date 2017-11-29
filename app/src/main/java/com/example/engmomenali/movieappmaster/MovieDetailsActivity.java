package com.example.engmomenali.movieappmaster;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static String TagId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView IM = (ImageView) findViewById(R.id.DetailsIV);
        TextView  TVOverView = (TextView) findViewById(R.id.DetailsOverView);
        TextView  TVUserRating = (TextView) findViewById(R.id.UserRating);
        TextView  TVReleaseDate = (TextView) findViewById(R.id.ReleaseDate);

            Bundle extras = getIntent().getExtras();


            setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
            TVOverView.setText(extras.getString(MovieContract.MovieEntry.OVERVIEW));
            TVUserRating.setText(extras.getString(MovieContract.MovieEntry.RATING));
            TVReleaseDate.setText( extras.getString(MovieContract.MovieEntry.RELEASEDATE));
            TagId = extras.getString(MovieContract.MovieEntry._ID);
            Picasso.with(this)
                    .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+extras.getString(MovieContract.MovieEntry.POSTERPATH))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(IM);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
