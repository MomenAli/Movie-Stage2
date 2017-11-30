package com.example.engmomenali.movieappmaster.sync;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.engmomenali.movieappmaster.Movie;

/**
 * Created by Momen Ali on 11/26/2017.
 */

public class MovieSyncUtils {

    public static void startImmediateSync(Context context) {
        Log.d("lifecycle", "startImmediateSync: ");

        Intent intent = new Intent(context, MovieSyncIntentServise.class);
        context.startService(intent);
    }
}
