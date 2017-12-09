package com.example.engmomenali.movieappmaster;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.data.MovieContract;
import com.example.engmomenali.movieappmaster.reviews.ReviewFragment;
import com.example.engmomenali.movieappmaster.trailers.TrailerFragment;
import com.example.engmomenali.movieappmaster.utils.URLParameters;
import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  , TrailerFragment.onTrailerLoadingFinishListener , ReviewFragment.onReviewLoadingFinishListener{

    /* the debugging TAG*/
    String TAG = "MovieDetailsActivity";

    /* const key used in the bundle of onSaveInstance*/
    private static final String top_key = "top_Key";

    /* const keys used by the bundle to define the operation to the bundle*/

    private static final String SQL_KEY = "key";
    private static final String SQL_FETCH = "fetch";
    private static final String SQL_INSERT = "insert";
    private static final String SQL_DELETE = "delete";
    String sqlTypeHolder;

    /*  variable hold the id of the current movie*/
    public static String tagId;

    /* boolean value represent if the current movie is checked as favorite or not*/
    public Boolean favorite = false;

    /* Movie Object hold the Current Movie*/
    Movie movie;

    /* variable hole the value of the distance between the current scrollView Position and the the Top*/
    int Top;

    /* loader id const*/
    private static final int _LOADER_ID = 930;


    /* boolean variables represent if the fetching of reviews and trailers has finished or not*/
    boolean fetchReviewFinished = false;
    boolean fetchTrailerFinished = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* set the resource of the activity*/
        setContentView(R.layout.activity_movie_details);

        /* setup the home action button in the ActionBar*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /* get the resources of the Views of the activity*/
        ImageView IM = (ImageView) findViewById(R.id.ivDetails);
        TextView TVOverView = (TextView) findViewById(R.id.tvDetailsOverView);
        TextView TVUserRating = (TextView) findViewById(R.id.tvUserRating);
        TextView TVReleaseDate = (TextView) findViewById(R.id.tvReleaseDate);
        ImageView favoriteStar = (ImageView) findViewById(R.id.ivFavorite_star);


        /* adding the fragment of the Trailers and Reviews to the Activity */

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.trailer_container, new TrailerFragment())
                .commit();

        fragmentManager.beginTransaction()
                .add(R.id.review_containter, new ReviewFragment())
                .commit();


        /* get the bundle that sent with the intent */
        Bundle extras = getIntent().getExtras();

        /* fetch the data from the bundle into my movie variable and the views*/
        movie = new Movie();
        setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
        movie.setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
        TVOverView.setText(extras.getString(MovieContract.MovieEntry.OVERVIEW));
        movie.setoverview(extras.getString(MovieContract.MovieEntry.OVERVIEW));
        TVUserRating.setText(extras.getString(MovieContract.MovieEntry.RATING));
        movie.setRatings(Double.parseDouble(extras.getString(MovieContract.MovieEntry.RATING)));
        TVReleaseDate.setText(extras.getString(MovieContract.MovieEntry.RELEASEDATE));
        movie.setReleaseDate(extras.getString(MovieContract.MovieEntry.RELEASEDATE));
        movie.setId(Long.parseLong(extras.getString(MovieContract.MovieEntry._ID)));
        tagId = extras.getString(MovieContract.MovieEntry._ID);
        movie.setPosterPath(extras.getString(MovieContract.MovieEntry.POSTERPATH));
        movie.setCoverImagePath(extras.getString(MovieContract.MovieEntry.COVERIMAGEPATH));
        movie.setPopularity(Double.parseDouble(extras.getString(MovieContract.MovieEntry.POPULARITY)));

        /* fetch the image using picasso */
       Picasso.with(this)
                .load(URLParameters.POSTER_URL + URLParameters.PHONE_SIZE + extras.getString(MovieContract.MovieEntry.POSTERPATH))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(IM);

       /* check if there is saved data from */
        if (savedInstanceState != null){
            Log.d(TAG, "onCreate: ");
            Top = savedInstanceState.getInt(top_key);

        }else Top = 0;


        /* create Bundle which will send with the loader */
        Bundle queryBundle = new Bundle();

        /* define the operation as fetching operation */
        queryBundle.putString(SQL_KEY,SQL_FETCH);
        /* initialize the Loader*/
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
        if (SearchLoader == null) {
            loaderManager.initLoader(_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /* get the Scroll View */
        ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);


        /* get the scroll position from the screen*/
        int top = sv.getScrollY();


        Log.d(TAG, "onSaveInstanceState: "+top);
        /* save the scrolled position in the bundle*/
        outState.putInt(top_key,top);
        /* calling the super to save the page features*/
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onResume() {

         /* create Bundle which will send with the loader */
        Bundle queryBundle = new Bundle();

        /* define the operation as fetching operation */
        queryBundle.putString(SQL_KEY,SQL_FETCH);
        /* initialize the Loader*/
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
        if (SearchLoader == null) {
            loaderManager.initLoader(_LOADER_ID, queryBundle, this);
        } else {
            loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
        }
            /* scroll to my precious position*/
            ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);
            Log.d(TAG, "onResume: " + Top);
            sv.scrollTo(0,Top );

        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id,final Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                /* check the type of the sql command*/
                switch (args.getString(SQL_KEY)) {
                    case SQL_FETCH:
                        /* the operation is fetching movie from the database */
                        Cursor c = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(tagId)), null, null, null, null);
                        sqlTypeHolder = SQL_FETCH;
                        /* return the result to the onLoadingFinish function */
                        return c;
                    case SQL_INSERT:
                        /* the operation is inserting Movie to the database*/
                        ContentValues contentValues = new ContentValues();
                        Log.d(TAG, "MakeAsFavorite: " + movie.toString());
                        contentValues.put(MovieContract.MovieEntry.POSTERPATH,movie.getPosterPath());
                        contentValues.put(MovieContract.MovieEntry._ID,movie.getId());
                        contentValues.put(MovieContract.MovieEntry.OVERVIEW,movie.getoverview());
                        contentValues.put(MovieContract.MovieEntry.RATING,movie.getRatings());
                        contentValues.put(MovieContract.MovieEntry.POPULARITY,movie.getPopularity());
                        contentValues.put(MovieContract.MovieEntry.TITLE,movie.getTitle());
                        contentValues.put(MovieContract.MovieEntry.RELEASEDATE,movie.getReleaseDate());
                        contentValues.put(MovieContract.MovieEntry.COVERIMAGEPATH,movie.getCoverImagePath());

                        getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
                        sqlTypeHolder = SQL_INSERT;
                        break;

                    case SQL_DELETE:
                        /* the operation is deleting a movie from the database */
                        getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(tagId)),null,null);
                        sqlTypeHolder = SQL_DELETE;
                        break;

                }
                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
           ImageView IM = (ImageView) findViewById(R.id.ivFavorite_star);
           switch (sqlTypeHolder) {
               case SQL_DELETE:
                   /* if the operation is deleting the movie from the database
                    * mark the movie as un favorite
                    * and put the star in the off mode */
                   IM.setImageResource(android.R.drawable.btn_star_big_off);
                   favorite = false;
                   break;
               case SQL_INSERT:
                   /* if the operation is inserting the movie into the database
                   *  mark the movie as favorite
                   *  then put the star image on the on mode*/
                   IM.setImageResource(android.R.drawable.btn_star_big_on);
                   favorite = true;
                   break;
               default:
                /* if the operation is fetching a movie from the database
                * check if the movie in the database so the movie is marked
                * as favorite and put the star image in the on mode
                * if the movie doesn't exist in the database so the movie marked
                * as un favorite and put the star image in the off mode*/
               if (data != null && data.getCount() != 0) {
                   Log.d(TAG, "onLoadFinished: " + data.getCount());
                   IM.setImageResource(android.R.drawable.btn_star_big_on);
                   favorite = true;
               } else {
                   IM.setImageResource(android.R.drawable.btn_star_big_off);
                   favorite = false;
               }
           }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
       loader = null;
    }



    /* this function handle the click on the star image */
    public void MakeAsFavorite(View view) {
        /* get the image resources */
        ImageView IM = (ImageView) findViewById(R.id.ivFavorite_star);
        /* if the movie marked as favorite
        *  mark the movie as un favorite and delete it from the database */
        if (favorite) {
            favorite = false;
            IM.setImageResource(android.R.drawable.btn_star_big_off);

            /* create Bundle which will send with the loader */
            Bundle queryBundle = new Bundle();

            /* define the operation as deleting operation */

            queryBundle.putString(SQL_KEY,SQL_DELETE);
             /* initialize the Loader*/
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
            if (SearchLoader == null) {
                loaderManager.initLoader(_LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
            }
            Log.d(TAG, "MakeAsFavorite: delete " + MovieContract.MovieEntry.buildMovieUri(Long.parseLong(tagId)));


        } else {
            /* if the marked as un favorite
            *  mark the movie as favorite and insert it into the database */

            IM.setImageResource(android.R.drawable.btn_star_big_on);
            favorite = true;
             /* create Bundle which will send with the loader */
            Bundle queryBundle = new Bundle();

        /* define the operation as inserting operation */
            queryBundle.putString(SQL_KEY,SQL_INSERT);
        /* initialize the Loader*/
            LoaderManager loaderManager = getSupportLoaderManager();
            Loader<String> SearchLoader = loaderManager.getLoader(_LOADER_ID);
            if (SearchLoader == null) {
                loaderManager.initLoader(_LOADER_ID, queryBundle, this);
            } else {
                loaderManager.restartLoader(_LOADER_ID, queryBundle, this);
            }

        }
    }



    /* this function is implementation of interface made in the Trailer fragment
    *  this function is invoked when the fragment finish fetching the trailer
    *  from the server so we can move the scroll to the previous position */
    @Override
    public void OnTrailerFragmentLoadingFinish() {
        /* mark fetching trailer as finished */
        fetchTrailerFinished = true;
        Log.d(TAG, "OnTrailerFragmentLoadingFinish: " + fetchReviewFinished + "  " + fetchTrailerFinished);

        /* check if the fetching of the trailer and reviews are both done
        * if they are done scrollTo my previous position*/
        if (fetchTrailerFinished && fetchReviewFinished){
            final ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);

            Log.d(TAG, "OnTrailerFragmentLoadingFinish: " + Top);

            /* this code is solution to my problem to scrollTo the previous position
            *  I found this solution in stackOverflow site */
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(0,Top);
                }
            });
        }

    }

    /* this function is implementation of interface made in the Review fragment
     *  this function is invoked when the fragment finish fetching the reviews
     *  from the server so we can move the scroll to the previous position */
    @Override
    public void OnReviewFragmentLoadingFinish() {

        /* mark fetching reviews as finished */
        fetchReviewFinished = true;


        Log.d(TAG, "OnReviewFragmentLoadingFinish: " + fetchReviewFinished + "  " + fetchTrailerFinished);


        /* check if the fetching of the trailer and reviews are both done
        * if they are done scrollTo my previous position*/
        if (fetchTrailerFinished && fetchReviewFinished){
            final ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);

            Log.d(TAG, "OnReviewFragmentLoadingFinish: " + Top);


            /* this code is solution to my problem to scrollTo the previous position
            *  I found this solution in stackOverflow site */
            sv.post(new Runnable() {
                @Override
                public void run() {
                    sv.scrollTo(0,Top);
                }
            });

        }
    }
}
