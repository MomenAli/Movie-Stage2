package com.example.engmomenali.movieappmaster;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.sync.MovieSyncUtils;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class MianMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = MianMovieFragment.class.getSimpleName();

    private  MovieAdapter mMovieAdabter;
    AsyncTask mLoadingData;
    String JSONResults;
    View rootView;
    GridView gridView;
    private static final int CURSOR_LOADER_ID = 579;
    //    ProgressBar mLoadingIndicator;
    private ArrayList<Movie> mMovies;
    int i = 0;
    Movie[] movie = {
            new Movie(i++,"it","animae","15/2/2014","//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ",15.0,25.0,"path"),
            new Movie(i++,"it","animae","15/2/2014","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ",15.0,25.0,"path"),
            new Movie(i++,"it","animae","15/2/2014","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ",15.0,25.0,"path"),
            new Movie(i++,"it","animae","15/2/2014","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ",15.0,25.0,"path"),
            new Movie(i++,"it","animae","15/2/2014","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg ",15.0,25.0,"path"),
    };

    public MianMovieFragment() {
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onActivityCreated: /////////////////////////////");
        Cursor c =
                getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                        new String[]{MovieEntry._ID},
                        null,
                        null,
                        null);

        if (c.getCount() == 0 ){
           // insertData
            insertdata();
        }

//        Cursor temp = c;
//        temp.moveToPosition(1);
//        Log.d(TAG, "onActivityCreated: "+temp.getString(temp.getColumnIndex(MovieEntry.TITLE)));
        // initialize loader
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);

    }

    public void insertdata(){
        Log.d(TAG, "insertdata: inserting this fucking data");

        MovieSyncUtils.startImmediateSync(getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Refresh){
            Log.d(TAG, "onOptionsItemSelected: Refresh ///////////////////////////////////");


            return true;
        }
        if(id == R.id.Most_pop){
            try {
                fetchMovies(1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        if(id == R.id.Most_rate){
            try {
                fetchMovies(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "fetchMovies: "+"Starting this fucking program");

          rootView = inflater.inflate(R.layout.fragment_main,container,false);
         mMovieAdabter = new MovieAdapter(getActivity(),null,0,CURSOR_LOADER_ID);
         MovieSyncUtils.startImmediateSync(getActivity());
//        TextView tv = (TextView) rootView.findViewById(R.id.massage);
////        tv.setText("Hello");
//        try {
//            fetchMovies(2);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdabter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Movie MovieClick = mMovieAdabter.getItem(i);
////
////                // i did search in stackoverflow for passing multiple string to another activity and i found bundle
////                Intent intent = new Intent(getActivity() , MovieDetailsActivity.class);
////
////                Bundle extras = new Bundle();
////
////                Log.d(TAG, "onItemClick: "+MovieClick.getoverview());
////                extras.putString("Title",MovieClick.getTitle());
////                extras.putString("PosterPath",MovieClick.getPosterPath());
////                extras.putString("overview",MovieClick.getoverview());
////                extras.putString("Ratings",String.valueOf(MovieClick.getRatings()));
////                extras.putString("ReleaseDate",MovieClick.getReleaseDate());
////
////                intent.putExtras(extras);
////                startActivity(intent);
////
////                mMovieAdabter.notifyDataSetChanged();
//            }
//        });

        return rootView;
    }

    public void fetchMovies(int Sort) throws JSONException {
        ConnectivityManager cm = (ConnectivityManager)this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            URL SearchUrl = NetworkUtils.buildUrl(Sort);
            Log.d(TAG, "fetchMovies: " + SearchUrl);
           // new AsyncQueryTask(this.getContext(), new dataFetchCompleteListner()).execute(SearchUrl);
        }else
            Toast.makeText(this.getContext(),R.string.ConError,Toast.LENGTH_LONG).show();
        }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       mMovieAdabter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdabter.swapCursor( null);
    }


//    public class dataFetchCompleteListner implements AsyncTaskCompleteListener<String>{
//
//    @Override
//    public void onTaskComplete(String result) {
//        if (result != null && !result.equals("")) {
//            Log.d(TAG, "fetchMovies: " + result);
//            try {
//                JSONResults = result;
//                mMovies = MovieJsonUtils.getMovies(rootView.getContext(), JSONResults);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.d(TAG, "onPostExecute: " + mMovies.get(0).getTitle());
//
//            mMovieAdabter = new MovieAdapter(getActivity(), mMovies);
//            Log.d(TAG, "onPostExecute: " + mMovieAdabter.getItem(0).getTitle());
//
//            gridView.setAdapter(mMovieAdabter);
//            mMovieAdabter.notifyDataSetChanged();
//        } else {
//            JSONResults = "";
//            Log.d(TAG, "fetchMovies: " + "Error in network");
//        }
//    }
//}



//    /*make  AsyncTask class the same as we take in the course
//    to make the queury made in the background
//     */
//
//    public class QueryTask extends AsyncTask<URL, Void, String> {
//
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////  //          mLoadingIndicator.setVisibility(View.VISIBLE);
////        }
//
//        @Override
//        protected String doInBackground(URL... params) {
//            URL searchUrl = params[0];
//            String SearchResults = null;
//            try {
//                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return SearchResults;
//        }
//
//        @Override
//        protected void onPostExecute(String SearchResults) {
//            if (SearchResults != null && !SearchResults.equals("")) {
//                JSONResults = SearchResults;
//                Log.d(TAG, "fetchMovies: "+JSONResults);
//                try {
//                    mMovies = MovieJsonUtils.getMovies(rootView.getContext(), JSONResults);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                Log.d(TAG, "onPostExecute: "+mMovies.get(0).getTitle());
//
//                mMovieAdabter = new MovieAdapter(getActivity(),mMovies );
//                Log.d(TAG, "onPostExecute: "+mMovieAdabter.getItem(0).getTitle());
//
//                    gridView.setAdapter(mMovieAdabter);
//                    mMovieAdabter.notifyDataSetChanged();
//            } else{
//                JSONResults = "";
//                Log.d(TAG, "fetchMovies: "+"Error in network");
//
//
//            }
//        }
//    }



}
