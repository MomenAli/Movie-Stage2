package com.example.engmomenali.movieappmaster;

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

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.sync.MovieSyncUtils;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class MianMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final String TAG = "MianMovieFragment";
    public static int Search_Sort = 2;
    private static final String CallBack_Key = "CallBack_Key";
    int Query_Sort_id;
    private  MovieAdapter mMovieAdabter;
    AsyncTask mLoadingData;
    String JSONResults;
    View rootView;
    GridView gridView;
    private static final int CURSOR_LOADER_ID = 579;
    //    ProgressBar mLoadingIndicator;
    private ArrayList<Movie> mMovies;
    int i = 0;


    public MianMovieFragment() {
        setRetainInstance(true);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null){
            Query_Sort_id = savedInstanceState.getInt(CallBack_Key);
        }else Query_Sort_id = -1;
        Log.d(TAG, "onCreate: "+Query_Sort_id);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CallBack_Key,Query_Sort_id);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG_life, "onActivityCreated: ");

        Log.d(TAG, "onActivityCreated: /////////////////////////////");
       /* Cursor c =
                getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                        new String[]{MovieEntry._ID},
                        null,
                        null,
                        null);
        
        if (c.getCount() == 0 ){
           // insertData
            insertdata();
        }*/


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
String TAG_life = "lifecycle";


    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        switch (Query_Sort_id){
            case R.id.favorite:

                String mSortOrder = MovieEntry.RATING + " DESC";
                String mSelection = MovieEntry.Favorit + "=?";
                String [] mSelectionArgs = new String[]{"1"};
                Cursor c =
                        getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                                null,
                                mSelection,
                                mSelectionArgs,
                                mSortOrder);
                mMovieAdabter.swapCursor(c);
                Log.d(TAG, "onResume: favorite");
                break;
            case R.id.Most_pop:

                mSortOrder = MovieEntry.POPULARITY + " DESC";
                c =
                        getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                mSortOrder);
                mMovieAdabter.swapCursor(c);
                Log.d(TAG, "onResume: Most_pop");
                break;
            case R.id.Most_rate:

                mSortOrder = MovieEntry.RATING + " DESC";
                c =
                        getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                mSortOrder);
                mMovieAdabter.swapCursor(c);
                Log.d(TAG, "onResume: Most_rate");
                break;

                default:
                    c =
                            getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                                    null,
                                    null,
                                    null,
                                    null);
                    mMovieAdabter.swapCursor(c);
                    Log.d(TAG, "onResume: default");


        }
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Query_Sort_id = id;
        Log.d(TAG, "onOptionsItemSelected: "+Query_Sort_id);
        if(id == R.id.favorite){

            Search_Sort = 1;
            MovieSyncUtils.startImmediateSync(getActivity());
            String mSortOrder = MovieEntry.RATING + " DESC";
            String mSelection = MovieEntry.Favorit + "=?";
            String [] mSelectionArgs = new String[]{"1"};
            Cursor c =
                    getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                            null,
                            mSelection,
                            mSelectionArgs,
                            mSortOrder);
            mMovieAdabter.swapCursor(c);
            return true;
        }
        if(id == R.id.Most_pop){

                Search_Sort = 1;
                MovieSyncUtils.startImmediateSync(getActivity());
                String mSortOrder = MovieEntry.POPULARITY + " DESC";
                Cursor c =
                        getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                mSortOrder);
                mMovieAdabter.swapCursor(c);
            return true;
        }
        if(id == R.id.Most_rate){
            Search_Sort = 1;
            MovieSyncUtils.startImmediateSync(getActivity());
            String mSortOrder = MovieEntry.RATING + " DESC";
            Cursor c =
                    getActivity().getContentResolver().query(MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            mSortOrder);
            mMovieAdabter.swapCursor(c);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }








    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "fetchMovies: "+"Starting this fucking program");

          rootView = inflater.inflate(R.layout.fragment_main,container,false);
         mMovieAdabter = new MovieAdapter(getActivity(),null,0,CURSOR_LOADER_ID);

        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdabter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor mCursor = mMovieAdabter.getCursor();
                mCursor.moveToPosition(i);

                // i did search in stackoverflow for passing multiple string to another activity and i found bundle
                Intent intent = new Intent(getActivity() , MovieDetailsActivity.class);

                Bundle extras = new Bundle();
                Log.d(TAG, "onItemClick: "+mCursor.getString(mCursor.getColumnIndex(MovieEntry.TITLE)));

                extras.putString(MovieEntry._ID,mCursor.getString(mCursor.getColumnIndex(MovieEntry._ID)));
                extras.putString(MovieEntry.TITLE,mCursor.getString(mCursor.getColumnIndex(MovieEntry.TITLE)));
                extras.putString(MovieEntry.POSTERPATH,mCursor.getString(mCursor.getColumnIndex(MovieEntry.POSTERPATH)));
                extras.putString(MovieEntry.OVERVIEW,mCursor.getString(mCursor.getColumnIndex(MovieEntry.OVERVIEW)));
                extras.putString(MovieEntry.RATING,mCursor.getString(mCursor.getColumnIndex(MovieEntry.RATING)));
                extras.putString(MovieEntry.POPULARITY,mCursor.getString(mCursor.getColumnIndex(MovieEntry.POPULARITY)));
                extras.putString(MovieEntry.COVERIMAGEPATH,mCursor.getString(mCursor.getColumnIndex(MovieEntry.COVERIMAGEPATH)));
                extras.putString(MovieEntry.RELEASEDATE,mCursor.getString(mCursor.getColumnIndex(MovieEntry.RELEASEDATE)));
                extras.putString(MovieEntry.Favorit,mCursor.getString(mCursor.getColumnIndex(MovieEntry.Favorit)));
                Log.d("MovieDetailsActivity", "onItemClick: favorite "+mCursor.getString(mCursor.getColumnIndex(MovieEntry.Favorit)));
                intent.putExtras(extras);
                startActivity(intent);

                mMovieAdabter.notifyDataSetChanged();
           }
        });

        return rootView;
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new CursorLoader(getActivity(),
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: ");
       mMovieAdabter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset: ");mMovieAdabter.swapCursor( null);
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
