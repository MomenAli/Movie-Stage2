package com.example.engmomenali.movieappmaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * Created by Momen Ali on 11/28/2017.
 */

public class ReviewFragment  extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    ListView listView;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int SEARCH_LOADER = 553;
    int Quary_Kind;
    private static final int Quary_fetch_trialers = 437;
    private static final int Quary_fetch_reviews = 800;
    private ReviewAdabter mReviewAdabter;
    Review[] reviews = new Review[]{new Review()};


    public ReviewFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchReviews();
        super.onActivityCreated(savedInstanceState);
    }

    public void fetchReviews(){
        Quary_Kind = Quary_fetch_reviews;


        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL +"/"+ MovieDetailsActivity.TagId + URLParameters.Fetch_reviews);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);
        Log.d("SearchResults", "fetchReviews: "+SearchUrl);
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
        View rootView = inflater.inflate(R.layout.fregment_trailer, container, false);

        mReviewAdabter = new ReviewAdabter(getActivity(), Arrays.asList(reviews));

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setAdapter(mReviewAdabter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Review review = mReviewAdabter.getItem(position);
                Uri uri = Uri.parse(review.getUrl());
                Log.d("SearchResults", "onItemClick: "+review.getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                startActivity(intent);

            }
        });

        return rootView;
    }

    @Override
    public Loader<String> onCreateLoader(int id,final Bundle args) {
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

                // COMPLETED (12) Copy the try / catch block from the AsyncTask's doInBackground method
                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL Url= new URL(Quary_Url);
                    String SearchResults = NetworkUtils.getResponseFromHttpUrl(Url);
                    Log.d("SearchResults", "loadInBackground11: "+SearchResults);
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
//        switch (Quary_Kind){
//            case Quary_fetch_trialers:
//
//                break;
//            case Quary_fetch_reviews:
//
//                break;
//        }
        if (data != null) {
            try {
                Log.d("SearchResults", "onLoadFinished: "+data);
                reviews = MovieJsonUtils.getReviews(getContext(), data);

                Log.d("SearchResults", "number of reviews: "+reviews.length);
                for (Review t:reviews
                        ) {
                    Log.d("SearchResults", "onLoadFinished: "+t.toString());
                }
                mReviewAdabter = new ReviewAdabter(getActivity(), Arrays.asList(reviews));
                listView.setAdapter(mReviewAdabter);
                mReviewAdabter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
