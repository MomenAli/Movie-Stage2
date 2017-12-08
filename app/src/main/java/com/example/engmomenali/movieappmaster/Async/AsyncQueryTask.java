package com.example.engmomenali.movieappmaster.Async;

import android.content.Context;
import android.os.AsyncTask;

import com.example.engmomenali.movieappmaster.Utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Momen Ali on 10/20/2017.
 */

public class AsyncQueryTask extends AsyncTask<URL, Void, String> {

    private static final String TAG = AsyncQueryTask.class.getSimpleName();

    String SearchResults;
    private Context context;
    private AsyncTaskCompleteListener<String> listener;

    public AsyncQueryTask(Context context, AsyncTaskCompleteListener<String> listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(URL... params) {
        URL searchUrl = params[0];
        String SearchResults = null;
        try {
            SearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return SearchResults;
    }

    @Override
    protected void onPostExecute(String SearchResults) {
        super.onPostExecute(SearchResults);
        listener.onTaskComplete(SearchResults);
    }
}
