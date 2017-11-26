package com.example.engmomenali.movieappmaster.sync;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Momen Ali on 11/26/2017.
 */

public class MovieSyncIntentServise extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MovieSyncIntentServise() {
        super("MovieSyncIntentServise");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Context context = MovieSyncIntentServise.this;
        MovieSyncTask.syncMovie(context);
    }
}
