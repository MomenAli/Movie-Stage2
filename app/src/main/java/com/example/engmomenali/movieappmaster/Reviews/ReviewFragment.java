package com.example.engmomenali.movieappmaster.Reviews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.MovieDetailsActivity;
import com.example.engmomenali.movieappmaster.R;
import com.example.engmomenali.movieappmaster.Trailers.TrailerFragment;
import com.example.engmomenali.movieappmaster.Utils.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.Utils.URLParameters;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Momen Ali on 11/28/2017.
 */

public class ReviewFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    public static String TAG = "ReviewFragment";
    RecyclerView recyclerView;
    ListView listView;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int SEARCH_LOADER = 553;
    public static int ReviewNumber;
    Review[] reviews = new Review[]{new Review()};
    ViewPager vp;
    static onReviewLoadingFinishListener  mCallback;
    public ReviewFragment() {
    }
    public interface onReviewLoadingFinishListener{
        void OnReviewFragmentLoadingFinish();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (onReviewLoadingFinishListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnReviewFragmentLoadingFinish");
        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchReviews();
        super.onActivityCreated(savedInstanceState);
    }

    public void fetchReviews() {

        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL + "/" + MovieDetailsActivity.TagId + URLParameters.Fetch_reviews);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);
        Log.d(TAG, "fetchReviews: " + SearchUrl);
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
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_review, container, false);
        setupRecyclerView(recyclerView);
        vp = (ViewPager) container.findViewById(R.id.review_viewpager);

        return recyclerView;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(), Arrays.asList(reviews)
        ));
    }

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
            Log.d(TAG, "onBindViewHolder: " + position + " from " + ReviewNumber);
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

                // COMPLETED (12) Copy the try / catch block from the AsyncTask's doInBackground method
                /* Parse the URL from the passed in String and perform the search */
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
                reviews = MovieJsonUtils.getReviews(getContext(), data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ViewGroup.LayoutParams params = vp.getLayoutParams();
            int high = 0;
            int lines = 0;
            int ch = 0;
            setupRecyclerView(recyclerView);
            // calculate the height of the viewpager
            for (Review r : reviews
                    ) {
                String[] ss = r.getContent().split("/n");
                for (String s : ss
                        ) {
                    int i = s.length() / 50;
                    i++;
                    lines += i;
                    high += i * 25;
                }
                high += 80;
            }

            params.height = high;
            vp.setLayoutParams(params);
        }
        ReviewNumber = reviews.length;
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
