package com.example.engmomenali.movieappmaster;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.stetho.Stetho;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String TAG = "MianMovieFragment";
        Log.d(TAG, "onCreate: MainActivity");
        if(savedInstanceState == null) {
            Log.d(TAG, "onCreate:  Null");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                    new MianMovieFragment()).commit();
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
        }
    }


}
