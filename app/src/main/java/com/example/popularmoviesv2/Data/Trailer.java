package com.example.popularmoviesv2.Data;

import com.example.popularmoviesv2.Utilities.Constants;

public class Trailer {
    private String mKey;
    private String mName;

    public Trailer(String key, String name) {
        mKey = key;
        mName = name;
    }

    public String getTrailerUrl() {
        return Constants.YOUTUBE_BASE_URL + mKey;
    }

    public String getName() {
        return mName;
    }

}
