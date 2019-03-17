package com.example.popularmoviesv2.UI;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.Utilities.QueryUtils;

import java.util.List;

public class MovieLoader extends AsyncTaskLoader<List<Movie>> {

    private final String mUrl;

    public MovieLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    //public ArrayList<Movie> loadInBackground() {
    public List<Movie> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchMoviesData(mUrl);
    }

}
