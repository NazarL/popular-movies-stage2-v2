package com.example.popularmoviesv2.UI;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.popularmoviesv2.Data.Trailer;
import com.example.popularmoviesv2.R;

import java.util.ArrayList;
import java.util.Objects;

public class TrailerAdapter extends ArrayAdapter<Trailer> {


    public TrailerAdapter(Activity context, ArrayList<Trailer> trailersArrayList) {
        super(context, 0, trailersArrayList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item_trailer, parent, false);
        }

        Trailer currentTrailer = getItem(position);

        TextView showTrailerNameTV = listItemView.findViewById(R.id.txv_trailer_name);
        showTrailerNameTV.setText(Objects.requireNonNull(currentTrailer).getName());

        return listItemView;
    }
}
