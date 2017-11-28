package com.example.engmomenali.movieappmaster;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Momen Ali on 11/27/2017.
 */

public class TrailerAdapter extends ArrayAdapter<Trailer> {
    public TrailerAdapter(@NonNull Context context, @NonNull List<Trailer> objects) {
        super(context,0, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Trailer trailer = this.getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_item,parent,false);
        TextView tv_label = (TextView) convertView.findViewById(R.id.tv_trailer_name);
        TextView tv_size = (TextView) convertView.findViewById(R.id.tv_trailer_size);
        int num = position + 1;
        tv_label.setText("Trailer "+ num);

        tv_size.setText(trailer.getSize());

        convertView.setTag(trailer.getId());
        return convertView;
    }
}
