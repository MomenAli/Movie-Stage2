package com.example.engmomenali.movieappmaster.reviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.MovieDetailsActivity;
import com.example.engmomenali.movieappmaster.R;
import com.example.engmomenali.movieappmaster.utils.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.utils.URLParameters;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Momen Ali on 11/28/2017.
 */

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {

    /* const TAG used for debugging purposes */
    public static String TAG = "ReviewFragment";

    /*const key using in the bundle which passed to the loader*/
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int SEARCH_LOADER = 553;


    /* object of the recycle view holds our recycleView */
    RecyclerView recyclerView;

    /* variable used to hold the number of reviews we fetch*/
    public static int ReviewNumber;

    /* array of reviews have an empty review avoiding null exception */
    Review[] reviews = new Review[]{new Review()};

    /* instance of the interface to invoke the method after finishing our job here
    *  made as static so i can use it in the recycleView inner class */
    static onReviewLoadingFinishListener  mCallback;

    /* constructor*/
    public ReviewFragment() {
    }

    /* interface we will implement in the details activity so we can
     * communicate with the details class and say we finish our job here
     * after finishing my job here i can use scrollTo function. */

    public interface onReviewLoadingFinishListener{
        void OnReviewFragmentLoadingFinish();
    }

    /* this function make sure that when class implement the interface
    *  it will implement the function too. */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (onReviewLoadingFinishListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnReviewFragmentLoadingFinish");
        }

    }


    /* when create the activity fetch the review from the server */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchReviews();
        super.onActivityCreated(savedInstanceState);
    }


    /* this function made to fetch the reviews from the server*/
    public void fetchReviews() {

        /* build our url using URLParameters class and inserting the id of
         the movie in the url */
        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL + "/" + MovieDetailsActivity.tagId + URLParameters.FETCH_REVIEWS);
        /* create a bundle to hold the information and  send with the loader */
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);
        Log.d(TAG, "fetchReviews: " + SearchUrl);
        /* create instance of the loaderManger to initialize the loader. */
        LoaderManager loaderManager = this.getLoaderManager();
        Loader<String> SearchLoader = loaderManager.getLoader(SEARCH_LOADER);
        if (SearchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER, queryBundle, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* inflate the recycle view with our xml file */
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_review, container, false);

        /* setup out recycle view */
        setupRecyclerView(recyclerView);

        return recyclerView;
    }

    /* this function made to setup the recycle by setting it's layout and the adapter */
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), Arrays.asList(reviews)
        ));
    }

    /* this class taken from the example you give me */
    public static class RecyclerViewAdapter
            extends RecyclerView.Adapter<ReviewFragment.RecyclerViewAdapter.ViewHolder>  {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Review> mValues;
        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Review mReview;

            public final View mView;

            public final TextView mTVAuthorNmae;

            public final TextView mTVReview;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTVAuthorNmae = (TextView) view.findViewById(R.id.tv_Review_author);
                mTVReview = (TextView) view.findViewById(R.id.tv_review_body);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTVAuthorNmae.getText();
            }
        }

        public RecyclerViewAdapter(Context context, List<Review> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
        }

        @Override
        public ReviewFragment.RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.review_item, parent, false);
            view.setBackgroundResource(mBackground);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ReviewFragment.RecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ReviewFragment.RecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mReview = mValues.get(position);
            Log.d(TAG, "onBindViewHolder: ");
            holder.mTVAuthorNmae.setText(holder.mReview.getAuthor());
            holder.mTVReview.setText((CharSequence) holder.mReview.getContent());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Uri uri = Uri.parse(holder.mReview.getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }
            });

            /* if we finish the reviews invoke the function in the class
            *  which implement our interface. */
           if (position == ReviewNumber -1) {
               onReviewLoadingFinishListener mCallBack = ReviewFragment.mCallback;
               mCallBack.OnReviewFragmentLoadingFinish();
           }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(getContext()) {
            @Override
            protected void onStartLoading() {
                if (args == null) {
                    return;
                }
                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String Quary_Url = args.getString(SEARCH_QUERY_URL_EXTRA);
                if (Quary_Url == null || TextUtils.isEmpty(Quary_Url)) {
                    return null;
                }

                /* return the JSON response of the http request */
                try {
                    URL Url = new URL(Quary_Url);
                    String SearchResults = NetworkUtils.getResponseFromHttpUrl(Url);
                    Log.d(TAG, "loadInBackground11: " + SearchResults);
                    return SearchResults;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

            }
        };

    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        if (data != null) {
            try {
                Log.d(TAG, "onLoadFinished: " + data);

                /* parsng the reviews using the function getReviews
                in the class MovieJsonUtils */
                reviews = MovieJsonUtils.getReviews(getContext(), data);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            setupRecyclerView(recyclerView);

        }
        /* save the fetching reviews number */
        ReviewNumber = reviews.length;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        loader = null;
    }
}
