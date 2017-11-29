package com.example.engmomenali.movieappmaster;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Momen Ali on 11/28/2017.
 */

public class ReviewAdabter extends ArrayAdapter<Review> {
    public ReviewAdabter(@NonNull Context context, @NonNull List<Review> objects) {
        super(context,0, objects);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Review review = this.getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_item,parent,false);
        TextView tv_author = (TextView) convertView.findViewById(R.id.tv_Review_author);
        TextView tv_body = (TextView) convertView.findViewById(R.id.tv_review_body);
        int num = position + 1;
        tv_author.setText(review.getAuthor());

        tv_body.setText(review.getContent());
        Log.d("SearchResults", "getView: content -> "+review.getContent());
        convertView.setTag(review.getUrl());
        return convertView;
    }
}
