package com.example.popularmoviesv2.UI;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.DetailsActivity;
import com.example.popularmoviesv2.R;
import com.example.popularmoviesv2.Utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final Context context;
    private static List<Movie> mMovies;

    public void setData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public MovieAdapter(Context context, ArrayList<Movie> movies) {
        this.context = context;
        mMovies = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater
                        .from(context)
                        .inflate(R.layout.list_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie currentMovie = mMovies.get(position);

        if (currentMovie != null) {
            Picasso
                    .get()
                    .load(Constants.POSTER_BASE_URL + currentMovie.getPosterPath())
                    .error(R.drawable.ic_android_black_24dp)
                    .into(holder.posterIMV);
        }

        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra(Constants.SELECTED_MOVIE, currentMovie);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mMovies != null)
            return mMovies.size();
        else
            return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView posterIMV;
        private final View parentView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.parentView = itemView;
            posterIMV = itemView.findViewById(R.id.imv_grid_poster);
        }
    }
}
