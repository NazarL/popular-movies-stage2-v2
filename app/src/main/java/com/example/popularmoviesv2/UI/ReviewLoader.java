package com.example.popularmoviesv2.UI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.popularmoviesv2.Data.Review;
import com.example.popularmoviesv2.Utilities.QueryUtils;

import java.util.List;

public class ReviewLoader extends AsyncTaskLoader<List<Review>> {

    private String mUrl;

    public  ReviewLoader (@NonNull Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Nullable
    @Override
    public List<Review> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    protected List<Review> onLoadInBackground() {
        if(mUrl == null) {
            return null;
        }
        return QueryUtils.fetchReviewsData(mUrl);
    }
}
