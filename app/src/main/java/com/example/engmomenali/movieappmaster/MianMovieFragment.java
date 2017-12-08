package com.example.engmomenali.movieappmaster;

import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import com.example.engmomenali.movieappmaster.Data.MovieContract.*;
import com.example.engmomenali.movieappmaster.Utils.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.sync.MovieSyncUtils;


/**
 * Created by MomenAli on 10/15/2017.
 */

public class MianMovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    /* TAG constant for the Debugging */
    private static final String TAG = "MianMovieFragment";

    /* Loader const ID*/
    private static final int _LOADER_ID = 579;

    /* Const used as Key for the bundle that send with the Loader */
    private static final String SEARCH_QUERY_URL_EXTRA = "Key";

    /* constant define the Connection Field and the favorite list used in the Loader */
    private static final String CONNECTION_FAILED = "error";
    private static final String FAVORITE_LIST = "favorite";


    /* Constant key variables for saveing the data on OnSaveInstanceState*/
    private static final String CALLBACK_SORTING_KEY = "CallBack_Key";
    private static final String CALLBACK_SCROLL_KEY = "CallBack_scroll";

    /*  variable for saveing the Sorting state and use it in
     *  on OnSaveInstanceState*/
    int querySortID = -1;

    /*  variable for saveing the gridView postion  and use it in
     *  on OnSaveInstanceState*/
    int top;


    /* my Movie adapter instance of MovieAdapter Class to hold our movie
    *  arrayList */
    private MovieAdapter mMovieAdabter;



    /* variable used to hold the View of the Main Fragment */
    View rootView;

    /* varible hold the girdView*/
    GridView gridView;

    /*  Sorting const values that send to the NetworkUtils */
    private static final int SORT_FAVORITE = 3;
    private static final int SORT_POPULAR = 1;
    private static final int SORT_RATING = 0;
    private static final int SORT_DEFAULT = 2;

    /* our Movies ArrayList*/
    private ArrayList<Movie> mMovies;


    /* Class Constructor*/
    public MianMovieFragment() {
        setHasOptionsMenu(true);
    }




      /* onSaveInstanceState Called before the Activity destroyed
       * to save some important data */

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Log.d(TAG, "onSaveInstanceState: " + querySortID);
        /*Saving the Sorting type id */
        outState.putInt(CALLBACK_SORTING_KEY, querySortID);
        /* get the gridView Position */
        int top = gridView.getFirstVisiblePosition();
        /* Save the Position in the bundle */
        outState.putInt(CALLBACK_SCROLL_KEY, top);

    }


    /* Create our Menu*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);

    }


    @Override
    public void onResume() {

        super.onResume();
    }

    /* handle the selection of the menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* get the selected Sorting Item */
        int id = item.getItemId();
        // save the sorting id
        querySortID = id;

        /* Create bundle which will send with the Loader */
        Bundle queryBundle = new Bundle();

        /* Check the id and save the Sorting Type to the Bundle*/
        if (id == R.id.favorite) {
            queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_FAVORITE);
        }
        if (id == R.id.Most_pop) {
            queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_POPULAR);
        }
        if (id == R.id.Most_rate) {
            queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_RATING);
        }

        /* Do Action just in Case the id equal our three Sorting Types */
        if (id == R.id.Most_rate || id == R.id.Most_pop || id == R.id.favorite) {
            /* get the loader manger and initialize the Loader or reset it
             * if it was Created before  */
            LoaderManager loaderManager = getLoaderManager();
            Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
            if (SearchLoader == null) {
                loaderManager.initLoader(_LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    /* the first function called in the Fragment so we will initialize our
         * Movie Adapter and GridView  */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Log.d(TAG, "onCreateView: ");
        /* inflate our main Fragment */
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        /* Check if there is any Saved Values */
        if (savedInstanceState != null) {
            querySortID = savedInstanceState.getInt(CALLBACK_SORTING_KEY);
            top = savedInstanceState.getInt(CALLBACK_SCROLL_KEY);
            //Log.d(TAG, "onCreateView: " + querySortID);
        }

        /* setup our GridView */
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdabter);

        /* using the advantage of adding action listener to the gridView */
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                /* get the Clicked item */
                Movie MovieClick = mMovieAdabter.getItem(i);

                // i did search in stackoverflow for passing multiple string to another activity and i found bundle
                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);

                Bundle extras = new Bundle();

                extras.putString(MovieEntry._ID, String.valueOf(MovieClick.getId()));
                extras.putString(MovieEntry.TITLE, MovieClick.getTitle());
                extras.putString(MovieEntry.POSTERPATH, MovieClick.getPosterPath());
                extras.putString(MovieEntry.OVERVIEW, MovieClick.getoverview());
                extras.putString(MovieEntry.RATING, String.valueOf(MovieClick.getRatings()));
                extras.putString(MovieEntry.POPULARITY, String.valueOf(MovieClick.getPopularity()));
                extras.putString(MovieEntry.COVERIMAGEPATH, MovieClick.getCoverImagePath());
                extras.putString(MovieEntry.RELEASEDATE, MovieClick.getReleaseDate());
                intent.putExtras(extras);
                startActivity(intent);

                mMovieAdabter.notifyDataSetChanged();
            }
        });

        /* Creating a bundle to save the sorting type*/
        Bundle queryBundle = new Bundle();
        /* check what is the type of sorting */
        switch (querySortID) {
            case R.id.favorite:
                queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_FAVORITE);
                break;
            case R.id.Most_pop:
                queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_POPULAR);
                break;
            case R.id.Most_rate:
                queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_RATING);
                break;
            default:
                /* if there is no saved instance this is the default search Sorting Type*/
                queryBundle.putInt(SEARCH_QUERY_URL_EXTRA, SORT_DEFAULT);
        }

        /* get the loader manger and initialize the Loader or reset it
             * if it was Created before  */
        LoaderManager loaderManager = getLoaderManager();
        Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
        if (SearchLoader == null) {
            loaderManager.initLoader(_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
        }

        /* return the Main fragment inflated View */
        return rootView;
    }



    /* implementation of the Loader Functions*/
    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        /* returning AsyncTask that will work in the background */
        return new AsyncTaskLoader<String>(this.getActivity()) {
            @Override
            protected void onStartLoading() {
                if (args == null) return;
                /* Starting the Task*/
                forceLoad();

            }

            @Override
            public String loadInBackground() {
                /* Check if there is network Connection available in the device */
                ConnectivityManager cm = (ConnectivityManager) this.getContext().getSystemService(this.getContext().CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();


                if (isConnected) {
                    /* the Connection is available begin to fetch movies*/

                    switch (args.getInt(SEARCH_QUERY_URL_EXTRA)) {
                        /* check if the user needs to see the favorite movies*/
                        case SORT_FAVORITE:
                            /* fetch the favorite movies from the database */
                            Cursor mCursor = getActivity().getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);

                            ArrayList<Movie> mTempMovies = new ArrayList<>();
                            for (int i = 0; i < mCursor.getCount(); i++) {
                                Movie temp = new Movie();
                                mCursor.moveToPosition(i);
                                temp.setId(mCursor.getInt(mCursor.getColumnIndex(MovieEntry._ID)));
                                temp.setCoverImagePath(mCursor.getString(mCursor.getColumnIndex(MovieEntry.COVERIMAGEPATH)));
                                temp.setPopularity(mCursor.getDouble(mCursor.getColumnIndex(MovieEntry.POPULARITY)));
                                temp.setoverview(mCursor.getString(mCursor.getColumnIndex(MovieEntry.OVERVIEW)));
                                temp.setPosterPath(mCursor.getString(mCursor.getColumnIndex(MovieEntry.POSTERPATH)));
                                temp.setRatings(mCursor.getDouble(mCursor.getColumnIndex(MovieEntry.RATING)));
                                temp.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MovieEntry.RELEASEDATE)));
                                temp.setTitle(mCursor.getString(mCursor.getColumnIndex(MovieEntry.TITLE)));
                                mTempMovies.add(temp);
                            }
                            /* save the fetched movies to our global movies ArrayList */
                            mMovies = mTempMovies;
                            /* return favorite const key to the onLoadingFinsh function */
                            return FAVORITE_LIST;

                        default:
                            /* build the URL*/
                            URL searchUrl = NetworkUtils.buildUrl(args.getInt(SEARCH_QUERY_URL_EXTRA));


                            /*fetching for Movies from the Server */
                            String SearchResults = null;
                            try {
                                SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            return SearchResults;
                    }


                } else {
                    /* if there is no internet Connection in the Device
                    *  return the Connection Failed const to the onLoadingFinish function */
                    return CONNECTION_FAILED;
                }
            }

        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String result) {

        /* check if the result is Connection Field*/
        if (result.equals(CONNECTION_FAILED)) {
            Toast.makeText(this.getContext(), R.string.ConError, Toast.LENGTH_LONG).show();
            return;
        }

        /* check if the sorting is favorite*/
        if (result.equals(FAVORITE_LIST)) {
            mMovieAdabter = new MovieAdapter(getActivity(), mMovies);
            gridView.setAdapter(mMovieAdabter);
            mMovieAdabter.notifyDataSetChanged();

        }


        /*check if the server return movies */
        if (result != null && !result.equals("")) {
            /* get the Movies from the JSON response */
            try {
                String JSONResults = result;
                mMovies = MovieJsonUtils.getMovies(rootView.getContext(), JSONResults);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            /* define the Movie Adapter with the Movie ArrayList */
            mMovieAdabter = new MovieAdapter(getActivity(), mMovies);

            /* set the New Adapter in the gridView */
            gridView.setAdapter(mMovieAdabter);
            mMovieAdabter.notifyDataSetChanged();

            /*Scroll the GridView to the last Visualized item if there is any saveInatanceState*/
            gridView.smoothScrollToPosition(top);
            top = 0;

        } else {

            /* if the response was null or empty Connection Error Toast will appear */
            Toast.makeText(this.getContext(), R.string.ConError, Toast.LENGTH_LONG).show();

        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
       loader = null;
    }


}
