package com.example.engmomenali.movieappmaster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Arrays;

/**
 * Created by Momen Ali on 11/27/2017.
 */

public class TrailerFragment extends Fragment {
    private TrailerAdapter mTrailerAdapter;
    Trailer[] trailers = {
            new Trailer("595e5ec7c3a36828a109a85a", "EuOlYPSEzSc", "1080"),
            new Trailer("5963e10b9251415a400cbcfa", "lI_Yau69onQ", "1080"),
            new Trailer("595e5ec7c3a36828a109a85a", "EuOlYPSEzSc", "1080"),
            new Trailer("595e5ec7c3a36828a109a85a", "EuOlYPSEzSc", "1080"),
    };

    public TrailerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fregment_trailer, container, false);

        mTrailerAdapter = new TrailerAdapter(getActivity(), Arrays.asList(trailers));

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview);
        listView.setAdapter(mTrailerAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer trailer = mTrailerAdapter.getItem(position);
                Uri uri = Uri.parse(URLParameters.BASIC_YOUTUBE_URL+trailer.getKey());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);

                startActivity(intent);

            }
        });

        return rootView;
    }
}
