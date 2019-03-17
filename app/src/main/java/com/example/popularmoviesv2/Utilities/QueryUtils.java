package com.example.popularmoviesv2.Utilities;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.Data.Review;
import com.example.popularmoviesv2.Data.Trailer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }


    public static String buildMovieStringUrl(String sortQuery) {
        Uri baseUri = Uri.parse(Constants.MOVIE_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(sortQuery)
                .appendQueryParameter(Constants.API_QUERY_PART, Constants.API_KEY)
                .build();

        String moviesStringUrl = null;

        try {
            moviesStringUrl = uriBuilder.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while building string URL", e);
        }

        return moviesStringUrl;
    }

    public static String buildTrailerStringUrl(String movieId) {
        Uri baseUri = Uri.parse(Constants.MOVIE_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(movieId)
                .appendPath(Constants.TRAILER_QUERY_PART)
                .appendQueryParameter(Constants.API_QUERY_PART,Constants.API_KEY)
                .build();

        String trailerStringUrl = null;
        try {
            trailerStringUrl = uriBuilder.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error building string trailer URL", e);
        }

        Log.i(LOG_TAG, trailerStringUrl);
        return trailerStringUrl;
    }

    public static String buildReviewStringUrl(String movieId) {
        Uri baseUri = Uri.parse(Constants.MOVIE_BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(movieId)
                .appendPath(Constants.REVIEW_QUERY_PART)
                .appendQueryParameter(Constants.API_QUERY_PART, Constants.API_KEY)
                .build();

        String reviewStringUrl = null;
        try {
            reviewStringUrl = uriBuilder.toString();
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error building string trailer URL", e);
        }

        return reviewStringUrl;
    }

    public static List<Movie> fetchMoviesData(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while making HTTP request", e);
        }

        return extractMovieFromJson(jsonResponse);
    }

    public static List<Trailer> fetchTrailersData(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while making HTTP request", e);
        }
        return extractTrailersFromJson(jsonResponse);
    }

    public static List<Review> fetchReviewsData(String stringUrl) {
        URL url = createUrl(stringUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error while making HTTP request", e);
        }

        return extractReviewsFromJson(jsonResponse);
    }


    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while creating URL", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error KeyResponse code" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving Movies JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    private static List<Movie> extractMovieFromJson(String streamOutput) {

        if (TextUtils.isEmpty(streamOutput)) {
            Log.i(LOG_TAG, "JSON stream output is empty.");
            return null;
        }

        List<Movie> movieArrayList = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(streamOutput);
            JSONArray jsonArray = rootJsonObject.getJSONArray(Constants.ARRAY_KEY_VALUE);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject movieJsonObject = jsonArray.getJSONObject(i);
                String movieId = movieJsonObject.getString(Constants.ID);
                String moviePosterPath = movieJsonObject.getString(Constants.POSTER_PATH);
                String movieTitle = movieJsonObject.getString(Constants.MOVIE_TITLE);
                String movieReleaseDate = movieJsonObject.getString(Constants.RELEASE_DATE);
                String movieDescription = movieJsonObject.getString(Constants.PLOT_SYNOPSIS);
                String movieRating = movieJsonObject.getString(Constants.RATING);

                movieArrayList.add(new Movie(movieId, moviePosterPath, movieTitle, movieReleaseDate,
                        movieDescription, movieRating));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while parsing JSON string response", e);
        }

        return movieArrayList;
    }

    private static List<Trailer> extractTrailersFromJson(String streamOutput) {
        if(TextUtils.isEmpty(streamOutput)) {
            Log.e(LOG_TAG, "JSON Trailers stream output is empty");
            return null;
        }

        List<Trailer> trailersArrayList = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(streamOutput);
            JSONArray jsonArray = rootJsonObject.getJSONArray(Constants.ARRAY_KEY_VALUE);

            for(int i = 0; i<jsonArray.length(); i++ ) {
                JSONObject trailerJsonObject = jsonArray.getJSONObject(i);
                String trailerType = trailerJsonObject.getString(Constants.VIDEO_TYPE);

                if(trailerType.equals(Constants.VIDEO_TYPE_TRAILER)) {
                    String trailerKey = trailerJsonObject.getString(Constants.VIDEO_KEY);
                    String trailerName = trailerJsonObject.getString(Constants.VIDEO_NAME);

                    trailersArrayList.add(new Trailer(trailerKey, trailerName));
                }
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while parsing JSON string response", e);
        }

        return trailersArrayList;
    }

    private static List<Review> extractReviewsFromJson(String streamOutput) {
        if(TextUtils.isEmpty(streamOutput)) {
            Log.e(LOG_TAG, "JSON Reviews stream output is empty");
            return null;
        }

        List<Review> reviewsArrayList = new ArrayList<>();

        try {
            JSONObject rootJsonObject = new JSONObject(streamOutput);
            JSONArray jsonArray = rootJsonObject.getJSONArray(Constants.ARRAY_KEY_VALUE);

            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject reviewJsonObject = jsonArray.getJSONObject(i);
                String reviewAuthor = reviewJsonObject.getString(Constants.REVIEW_AUTHOR);
                String reviewContent = reviewJsonObject.getString(Constants.REVIEW_CONTENT);

                reviewsArrayList.add(new Review(reviewAuthor, reviewContent));
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Error while parsing JSON string response", e);
        }

        return reviewsArrayList;
    }

}
