package com.example.engmomenali.movieappmaster;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.example.engmomenali.movieappmaster.Utils.URLParameters;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    Context mContext;
    private static final String TAG = "MianMovieFragment";

    public MovieAdapter( Activity context, List<Movie> objects) {

        super(context,0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Log.d(TAG, "getView: " +position);
        Movie mMovie = getItem(position);
        mContext = parent.getContext();
        //if(convertView == null){
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent,false);
        ImageView IM = (ImageView) convertView.findViewById(R.id.IM_PosterImage);
        Picasso.with(mContext)
                .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+mMovie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(IM);
        // }

        return convertView;
    }
}
