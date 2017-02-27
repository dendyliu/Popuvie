package com.example.android.popuvie.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.popuvie.R;
import com.example.android.popuvie.model.Review;
import com.example.android.popuvie.model.Trailer;

import java.util.List;

/**
 * Created by Asus X550ZE on 12/8/2016.
 */

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity context, List<Review> reviews) {
        super(context, 0, reviews);

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review review = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.review_item, parent, false);
        }

        TextView reviewerName = (TextView) convertView.findViewById(R.id.reviewer_name);
        reviewerName.setText(review.getAuthor());

        TextView reviewerContent = (TextView) convertView.findViewById(R.id.reviewer_content);
        String content = review.getContent() + " from "+review.getUrl();
        reviewerContent.setText(content);

        return convertView;

    }
}
