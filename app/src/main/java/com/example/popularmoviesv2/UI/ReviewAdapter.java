package com.example.popularmoviesv2.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularmoviesv2.Data.Review;
import com.example.popularmoviesv2.R;

import java.util.ArrayList;
import java.util.Objects;

public class ReviewAdapter extends ArrayAdapter<Review> {

    public ReviewAdapter(Activity context, ArrayList<Review> reviewsArrayList) {
        super(context, 0, reviewsArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(convertView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_review, parent, false);
        }

        Review currentReview = getItem(position);

        TextView showReviewersName = listItemView.findViewById(R.id.txv_show_review_author);
        TextView showReviewText = listItemView.findViewById(R.id.txv_show_review_text);

        showReviewersName.setText(Objects.requireNonNull(currentReview).getAuthor());
        showReviewText.setText(currentReview.getContent());


        return listItemView;
    }
}
