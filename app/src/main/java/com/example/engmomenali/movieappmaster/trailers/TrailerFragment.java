package com.example.engmomenali.movieappmaster.trailers;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * Created by Momen Ali on 11/27/2017.
 */

public class TrailerFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    /* const key used in the bundle which send to the loader*/
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    /* loader const number */
    private static final int SEARCH_LOADER = 552;

    /* object from the interface to call the function when finished
    *  made as static so i can load it from the RecycleView  inner class */
    static onTrailerLoadingFinishListener mCallback;

    /* object from RecycleView*/
    RecyclerView recyclerView;

    /* TAG with the name of the class for the debugging purposes */
    final String TAG = "TrailerFragment";
    /* variable hold the number of trailer  we will fetch so we can
    *  know when our work finish*/
    public static int TrailerNumber;

    /* trailer array hold an empty trailer avoiding error happened because of null */
    Trailer[] trailers = new Trailer[]{new Trailer()};

    /* empty constructor */
    public TrailerFragment() {

    }

    /* Trailer interface*/
    public interface onTrailerLoadingFinishListener{
        void OnTrailerFragmentLoadingFinish();
    }

    /* this function made to be sure that when any one implement our interface he will
    * implement our function too*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mCallback = (onTrailerLoadingFinishListener)context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnTrailerFragmentLoadingFinish");
        }

    }

    /* when the fragment created fetch the trailer */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchTrailers();
        super.onActivityCreated(savedInstanceState);
    }

    /* this function made to fetch the trailers from the server */
    public void fetchTrailers(){

        /* build the URl using URLParameters class and inserting the id of the movie in the url */
        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL +"/"+ MovieDetailsActivity.tagId + URLParameters.FETCH_TRAILERS);
        /*create bundle to send it with the loader */
        Bundle queryBundle = new Bundle();
         queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);
        /* create instance of the loader manger to initialize the loader */
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
        /* inflate our recycleView*/
        recyclerView = (RecyclerView) inflater.inflate(
                R.layout.fragment_trailer, container, false);
        /* setup the  recycleView*/
        setupRecyclerView(recyclerView);

        return recyclerView;
    }

    /* this function setup the recycle View putting the layout
    * and the adapter*/
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),Arrays.asList(trailers)
                ));
    }
    /* this class taken from the example you give to me */
    public static class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Trailer> mValues;
        Context mContext;
        String TAG = "TrailerFragment";

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public Trailer mTrailer;

            public final View mView;

            public final TextView mTVTrailerNmae;

            public final TextView mTVSize;
            
            
            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTVTrailerNmae = (TextView) view.findViewById(R.id.tv_trailer_name);
                mTVSize = (TextView) view.findViewById(R.id.tv_trailer_size);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTVTrailerNmae.getText();
            }
        }

        public RecyclerViewAdapter(Context context, List<Trailer> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
            mBackground = mTypedValue.resourceId;
            mValues = items;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_item, parent, false);
            view.setBackgroundResource(mBackground);
            Log.d(TAG, "onCreateViewHolder: ");
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTrailer = mValues.get(position);
            int num = position+1;
            Log.d(TAG, "onBindViewHolder: ");
            holder.mTVTrailerNmae.setText("Trailer "+num);
            holder.mTVSize.setText((CharSequence) holder.mTrailer.getSize());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Uri uri = Uri.parse(URLParameters.BASIC_YOUTUBE_URL+holder.mTrailer.getKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(intent);
                    }
                }
            });
            /* if the trailers finished
            * invoke the function that implemented with the interface*/
            if (position == TrailerNumber -1) {
                onTrailerLoadingFinishListener mCallBack = TrailerFragment.mCallback;
                Log.d(TAG, "onBindViewHolder: OnTrailerFragmentLoadingFinish");
                mCallBack.OnTrailerFragmentLoadingFinish();
            }

        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
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

                /* return the JSON response from the http request  */
                try {
                    URL Url= new URL(Quary_Url);
                    String SearchResults = NetworkUtils.getResponseFromHttpUrl(Url);
                    Log.d("TAG", "loadInBackground: "+SearchResults);
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
                Log.d("TAG", "onLoadFinished: "+data);
                /* parsing the trailers from the JSON using the getTrailers function
                 * in the MovieJsonUtils class */
                trailers = MovieJsonUtils.getTrailers(getContext(), data);

                Log.d("TAG", "number of movies: "+trailers.length);

                /* resetup of recycleView */
                setupRecyclerView(recyclerView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        /* get the number of the trailers fetched*/
        TrailerNumber = trailers.length;

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
     loader = null;
    }

}
