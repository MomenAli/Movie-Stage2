package com.example.engmomenali.movieappmaster;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.Data.MovieContract;
import com.example.engmomenali.movieappmaster.Reviews.ReviewFragment;
import com.example.engmomenali.movieappmaster.Trailers.TrailerFragment;
import com.example.engmomenali.movieappmaster.Utils.URLParameters;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  , TrailerFragment.onTrailerLoadingFinishListener , ReviewFragment.onReviewLoadingFinishListener{

    /* the debugging TAG*/
    String TAG = "MovieDetailsActivity";

    /* const key used in the bundle of onSaveInstance*/
    private static final String top_key = "top_Key";


    /*  variable hold the id of the current movie*/
    public static String TagId;

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
        ImageView IM = (ImageView) findViewById(R.id.DetailsIV);
        TextView TVOverView = (TextView) findViewById(R.id.DetailsOverView);
        TextView TVUserRating = (TextView) findViewById(R.id.UserRating);
        TextView TVReleaseDate = (TextView) findViewById(R.id.ReleaseDate);
        ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star);

        Log.d(TAG, "onCreate: "+ Top);

        /* */
        Bundle extras = getIntent().getExtras();
        ViewPager viewPager = (ViewPager) findViewById(R.id.trailer_viewpager);
        if (viewPager != null) {
            Adapter adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new TrailerFragment(), "Category 1");
            viewPager.setAdapter(adapter);
        }
        ViewPager viewPager2 = (ViewPager) findViewById(R.id.review_viewpager);
        if (viewPager2 != null) {
            Adapter adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(new ReviewFragment(), "Category 1");
            viewPager2.setAdapter(adapter);
        }
        movie = new Movie();
        setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
        movie.setTitle(extras.getString(MovieContract.MovieEntry.TITLE));
        TVOverView.setText(extras.getString(MovieContract.MovieEntry.OVERVIEW));
        movie.setoverview(extras.getString(MovieContract.MovieEntry.OVERVIEW));
        TVUserRating.setText(extras.getString(MovieContract.MovieEntry.RATING));
        movie.setRatings(extras.getDouble(MovieContract.MovieEntry.RATING));
        TVReleaseDate.setText(extras.getString(MovieContract.MovieEntry.RELEASEDATE));
        movie.setReleaseDate(extras.getString(MovieContract.MovieEntry.RELEASEDATE));
        movie.setId(Long.parseLong(extras.getString(MovieContract.MovieEntry._ID)));
        TagId = extras.getString(MovieContract.MovieEntry._ID);
        movie.setPosterPath(extras.getString(MovieContract.MovieEntry.POSTERPATH));
        movie.setCoverImagePath(extras.getString(MovieContract.MovieEntry.COVERIMAGEPATH));
        movie.setPopularity(extras.getDouble(MovieContract.MovieEntry.POPULARITY));
        Log.d(TAG, "onCreate: " + movie.toString());
        Log.d(TAG, "onCreate: "+TagId);
       // Log.d("MovieDetailsActivity", "onItemClick: favorite " + extras.getString(MovieContract.MovieEntry.Favorit));

 /*       if (extras.getString(MovieContract.MovieEntry.Favorit).equals("0")) {
            Favorate = false;
            favoriteStar.setImageResource(android.R.drawable.btn_star_big_off);
        } else {
            Favorate = true;
            favoriteStar.setImageResource(android.R.drawable.btn_star_big_on);
        }

 */       Picasso.with(this)
                .load(URLParameters.POSTER_URL + URLParameters.PHONE_SIZE + extras.getString(MovieContract.MovieEntry.POSTERPATH))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(IM);

        if (savedInstanceState != null){
            Log.d(TAG, "onCreate: ");
            Top = savedInstanceState.getInt(top_key);

        }else Top = 0;

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> githubSearchLoader = loaderManager.getLoader(_LOADER_ID);
        if (githubSearchLoader == null) {
            loaderManager.initLoader(_LOADER_ID, null, this);
        } else {
            loaderManager.restartLoader(_LOADER_ID, null, this);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /*View rootView = findViewById(android.R.id.content);
        int top = (int) rootView.getY();

        Log.d(TAG, "onSaveInstanceState: "+top);

        outState.putInt(top_key,top);
        */
        ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);
        int top = sv.getScrollY();

        Log.d(TAG, "onSaveInstanceState: "+top);
        outState.putInt(top_key,top);

    }

    @Override
    protected void onResume() {

        ImageView favoriteStar = (ImageView) findViewById(R.id.favorite_star);
        Cursor mCursor = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)), null, null, null, null);
        if (mCursor.getCount() == 0) {
                favorite = false;
                favoriteStar.setImageResource(android.R.drawable.btn_star_big_off);
            } else {
                favorite = true;
                favoriteStar.setImageResource(android.R.drawable.btn_star_big_on);
            }
            ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);
            Log.d(TAG, "onResume: " + Top);
            sv.scrollTo(0,Top);

        super.onResume();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        return new AsyncTaskLoader<Cursor>(this) {
            @Override
            protected void onStartLoading() {
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {

                Cursor c = getContentResolver().query(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)),null,null,null,null);

                return c;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "onLoadFinished: " + data.getCount());
           ImageView IM = (ImageView) findViewById(R.id.favorite_star);
           if (data != null && data.getCount() != 0){
               IM.setImageResource(android.R.drawable.btn_star_big_on);
               favorite = true;
           }else{
               IM.setImageResource(android.R.drawable.btn_star_big_off);
               favorite = false;
           }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void MakeAsFavorite(View view) {
        ImageView IM = (ImageView) findViewById(R.id.favorite_star);
        if (favorite) {
            favorite = false;
            IM.setImageResource(android.R.drawable.btn_star_big_off);
            getContentResolver().delete(MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)), null, null);
            Log.d(TAG, "MakeAsFavorite: delete " + MovieContract.MovieEntry.buildMovieUri(Long.parseLong(TagId)));


        } else {

            IM.setImageResource(android.R.drawable.btn_star_big_on);
            favorite = true;
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
        }
    }

    public String PrintCursor(Cursor mCursor) {
        if (mCursor.getCount() == 0) return "/////////empty";
        mCursor.moveToPosition(0);
        String ss = MovieContract.MovieEntry._ID + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry._ID)) + " " +
                MovieContract.MovieEntry.TITLE + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.TITLE)) + " " +
                MovieContract.MovieEntry.POSTERPATH + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POSTERPATH)) + " " +
                MovieContract.MovieEntry.OVERVIEW + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.OVERVIEW)) + " " +
                MovieContract.MovieEntry.RATING + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RATING)) + " " +
                MovieContract.MovieEntry.POPULARITY + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.POPULARITY)) + " " +
                MovieContract.MovieEntry.COVERIMAGEPATH + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.COVERIMAGEPATH)) + " " +
                MovieContract.MovieEntry.RELEASEDATE + " " + mCursor.getString(mCursor.getColumnIndex(MovieContract.MovieEntry.RELEASEDATE));
        return ss;
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
    @Override
    public void OnTrailerFragmentLoadingFinish() {
        fetchTrailerFinished = true;
        Log.d(TAG, "OnTrailerFragmentLoadingFinish: " + fetchReviewFinished + "  " + fetchTrailerFinished);
        if (fetchTrailerFinished && fetchReviewFinished){
            ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);

            Log.d(TAG, "OnTrailerFragmentLoadingFinish: " + Top);
            sv.scrollTo(0,Top);
        }

    }

    @Override
    public void OnReviewFragmentLoadingFinish() {
        fetchReviewFinished = true;

        Log.d(TAG, "OnReviewFragmentLoadingFinish: " + fetchReviewFinished + "  " + fetchTrailerFinished);
        if (fetchTrailerFinished && fetchReviewFinished){
            ScrollView sv = (ScrollView) findViewById(R.id.SV_Details);

            Log.d(TAG, "OnReviewFragmentLoadingFinish: " + Top);
            sv.scrollTo(0,Top);
        }
    }
}
