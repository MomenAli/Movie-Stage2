package com.example.engmomenali.movieappmaster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

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


            setTitle(extras.getString("Title"));
            TVOverView.setText("Overview:"+extras.getString("overview"));
            TVUserRating.setText("Rating:" + extras.getString("Ratings"));
            TVReleaseDate.setText("Release Date: "+ extras.getString("ReleaseDate"));

            Picasso.with(this)
                    .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+extras.getString("PosterPath"))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(IM);

    }
}
