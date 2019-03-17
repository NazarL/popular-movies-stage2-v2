package com.example.popularmoviesv2.Utilities;

import com.example.popularmoviesv2.BuildConfig;

public class Constants {
    public static final String SELECTED_MOVIE = "selectedMovie";

    public static final String ARRAY_KEY_VALUE = "results";
    public static final String MOVIE_TITLE = "title";
    public static final String POSTER_PATH = "poster_path";
    public static final String RELEASE_DATE = "release_date";
    public static final String PLOT_SYNOPSIS = "overview";
    public static final String RATING = "vote_average";

    public static final String API_KEY = BuildConfig.ApiKey;
    public static final String API_QUERY_PART = "api_key";
    public static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w342/";

    //Trailers
    public static final String VIDEO_TYPE_TRAILER = "Trailer";
    public static final String VIDEO_KEY = "key";
    public static final String VIDEO_NAME = "name";
    public static final String VIDEO_TYPE = "type";

    public static final String ID = "id";
    public static final String TRAILER_QUERY_PART = "videos";
    public static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    //Reviews
    public static final String REVIEW_QUERY_PART = "reviews";
    public static final String REVIEW_AUTHOR = "author";
    public static final String REVIEW_CONTENT = "content";
}
