package com.example.engmomenali.movieappmaster;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Utils.URLParameters;
import com.squareup.picasso.Picasso;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class MovieAdapter extends CursorAdapter {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    Context mContext;
    private static int sLoaderID ;

    public MovieAdapter(Context context, Cursor c, int flags,int Loader) {
        super(context, c, flags);
        Log.d(LOG_TAG, "MovieAdapter: trying to add Movie");
        sLoaderID = Loader;
        mContext = context;
    }

    public static class ViewHolder{
        public final ImageView imageView;

        public ViewHolder(View View) {
            this.imageView = (ImageView) View.findViewById(R.id.IM_PosterImage);
        }
    }

//    public MovieAdapter( Activity context, List<Movie> objects) {
//
//        super(context,0, objects);
//    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        Movie mMovie = getItem(position);
//        mContext = parent.getContext();
//        //if(convertView == null){
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent,false);
//            ImageView IM = (ImageView) convertView.findViewById(R.id.IM_PosterImage);
//            Picasso.with(mContext)
//            .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+mMovie.getPosterPath())
//            .placeholder(R.drawable.placeholder)
//                    .error(R.drawable.error)
//                    .into(IM);
//       // }
//
//        return convertView;
//    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.movie_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);
        return view;
    }



    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        String posterPath = cursor.getString(cursor.getColumnIndex(MovieEntry.POSTERPATH));
        Log.d(LOG_TAG, "bindView: "+posterPath);

        Picasso.with(mContext)
            .load(URLParameters.POSTER_URL+URLParameters.PHONE_SIZE+posterPath)
            .placeholder(R.drawable.placeholder)
                    .error(R.drawable.error)
                    .into(viewHolder.imageView);
    }
}
