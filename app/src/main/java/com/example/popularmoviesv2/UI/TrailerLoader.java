package com.example.popularmoviesv2.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesv2.Data.Trailer;
import com.example.popularmoviesv2.Utilities.QueryUtils;

import java.util.List;

public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {

    private String mUrl;

    public TrailerLoader(@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<Trailer> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    protected List<Trailer> onLoadInBackground() {
        if(mUrl == null) {
            return null;
        }
        return QueryUtils.fetchTrailersData(mUrl);
    }
}
