package com.example.engmomenali.movieappmaster;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.engmomenali.movieappmaster.Utils.MovieJsonUtils;
import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;
import com.example.engmomenali.movieappmaster.Utils.URLParameters;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Momen Ali on 11/27/2017.
 */

public class TrailerFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{

    RecyclerView recyclerView;
    private static final String SEARCH_QUERY_URL_EXTRA = "query";
    private static final int SEARCH_LOADER = 552;
    int Quary_Kind;
    private static final int Quary_fetch_trialers = 437;
    private static final int Quary_fetch_reviews = 800;

    Trailer[] trailers = new Trailer[]{new Trailer()};
    ViewPager vp;
   /* TextView tv_label;
    ImageView IMSperate;*/

    public TrailerFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        fetchTrailers();
        super.onActivityCreated(savedInstanceState);
    }
    public void fetchTrailers(){
        Quary_Kind = Quary_fetch_trialers;

        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL +"/"+ MovieDetailsActivity.TagId + URLParameters.Fetch_trailers);
      Bundle queryBundle = new Bundle();
         queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);

        LoaderManager loaderManager = this.getLoaderManager();
        Loader<String> SearchLoader = loaderManager.getLoader(SEARCH_LOADER);
         if (SearchLoader == null) {
            loaderManager.initLoader(SEARCH_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(SEARCH_LOADER, queryBundle, this);
        }
    }
    public void fetchReviews(){
        Quary_Kind = Quary_fetch_reviews;

        String id = getTag();
        Log.d("SearchResults", "fetchReviews: "+id);

        String SearchUrl =
                NetworkUtils.buildUrl(URLParameters.MOVIE_DB_SITE_URL +"/"+ id + URLParameters.Fetch_reviews);
        Bundle queryBundle = new Bundle();
        queryBundle.putString(SEARCH_QUERY_URL_EXTRA, SearchUrl);

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
                R.layout.fregment_trailer, container, false);
        setupRecyclerView(recyclerView);
        vp = (ViewPager) container.findViewById(R.id.trailer_viewpager);
        /*tv_label = (TextView) container.findViewById(R.id.tv_trailers_label);
        IMSperate = (ImageView) container.findViewById(R.id.IMSeprate);*/

        return recyclerView;
    }
    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new RecyclerViewAdapter(getActivity(),Arrays.asList(trailers)
                ));
    }
    public static class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        private List<Trailer> mValues;

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
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trailer_item, parent, false);
            view.setBackgroundResource(mBackground);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTrailer = mValues.get(position);
            int num = position+1;
            holder.mTVTrailerNmae.setText("Trailer "+num);
            holder.mTVSize.setText((CharSequence) holder.mTrailer.getSize());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Uri uri = Uri.parse(URLParameters.BASIC_YOUTUBE_URL+holder.mTrailer.getKey());
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(uri);

                    context.startActivity(intent);
                }
            });


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

                // COMPLETED (12) Copy the try / catch block from the AsyncTask's doInBackground method
                /* Parse the URL from the passed in String and perform the search */
                try {
                    URL Url= new URL(Quary_Url);
                    String SearchResults = NetworkUtils.getResponseFromHttpUrl(Url);
                    Log.d("SearchResults", "loadInBackground: "+SearchResults);
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
                Log.d("SearchResults", "onLoadFinished: "+data);
                trailers = MovieJsonUtils.getTrailers(getContext(), data);

                Log.d("SearchResults", "number of movies: "+trailers.length);
                for (Trailer t:trailers
                     ) {
                    Log.d("SearchResults", "onLoadFinished: "+t.toString());
                }

                ViewGroup.LayoutParams params = vp.getLayoutParams();
                int high = 93 * trailers.length;
                params.height = high;
                vp.setLayoutParams(params);
                setupRecyclerView(recyclerView);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

}
