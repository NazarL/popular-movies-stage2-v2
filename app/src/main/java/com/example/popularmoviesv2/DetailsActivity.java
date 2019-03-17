package com.example.popularmoviesv2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.Data.Review;
import com.example.popularmoviesv2.Data.Trailer;
import com.example.popularmoviesv2.UI.ReviewAdapter;
import com.example.popularmoviesv2.UI.ReviewLoader;
import com.example.popularmoviesv2.UI.TrailerAdapter;
import com.example.popularmoviesv2.UI.TrailerLoader;
import com.example.popularmoviesv2.Utilities.Constants;
import com.example.popularmoviesv2.Utilities.QueryUtils;
import com.example.popularmoviesv2.ViewModel.MovieViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    @BindView(R.id.txv_movie_title)
    TextView originalTitleTV;
    @BindView(R.id.imv_poster_thumbnail)
    ImageView posterThumbnailIMV;
    @BindView(R.id.txv_user_rating)
    TextView userRatingTV;
    @BindView(R.id.txv_release_date)
    TextView releaseDateTV;
    @BindView(R.id.txv_overview)
    TextView movieOverviewTV;
    @BindView(R.id.lsv_trailers_list)
    ListView trailerLV;
    @BindView(R.id.lsv_reviews_list)
    ListView reviewLV;
    @BindView(R.id.btn_favorites)
    Button favoriteButton;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    public MovieViewModel movieViewModel;
    private String movieId;
    public Movie mMovie;
    boolean isFavorite = false;

    private static final int TRAILERS_LOADER = 0;
    private static final int REVIEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        mMovie = getIntent().getParcelableExtra(Constants.SELECTED_MOVIE);
        movieId = mMovie.getId();

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.findMovieById(movieId).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie movie) {
                if(mMovie.equals(movie)) {
                    Log.d(LOG_TAG, "movieViewModel.findMovieById(movieId) movie is in Db = true");
                    isFavorite = true;
                } else {
                    Log.d(LOG_TAG, "movieViewModel.findMovieById(movieId) movie is in Db = false");
                    isFavorite = false;
                }
            }
        });

        setAdapters();

        if (mMovie != null) {
            displayUI(mMovie);
            getSupportLoaderManager().initLoader(TRAILERS_LOADER, null, trailersLoader);
            getSupportLoaderManager().initLoader(REVIEWS_LOADER, null, reviewsLoader);
        }
    }

    private void insertMovie() {
        Log.d(LOG_TAG, "insertMovie() isFavorite = false");
        movieViewModel.insertMovie(mMovie);
        isFavorite = true;
        Log.d(LOG_TAG, "insertMovie() isFavorite = true");
        Toast toast = Toast.makeText(
                getApplicationContext(), R.string.toast_added_favorites, Toast.LENGTH_LONG);
        toast.show();
        favoriteButton.setText(R.string.delete_from_favorites);
    }

    private void deleteMovie() {
        Log.d(LOG_TAG, "deleteMovie() movie is in Db = true");
        movieViewModel.deleteMovie(mMovie);
        isFavorite = false;
        Log.d(LOG_TAG, "deleteMovie() movie is in Db = false");
        Toast toast = Toast.makeText(
                getApplicationContext(), R.string.toast_deleted_favorites, Toast.LENGTH_LONG);
        toast.show();
        favoriteButton.setText(R.string.add_to_favorites);
    }

    private void manageFavorites() {
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorite) {
                    Log.d(LOG_TAG, "manageFavorites() movie is in Db = true");
                    deleteMovie();
                } else {
                    Log.d(LOG_TAG, "manageFavorites() movie is in Db = false");
                    insertMovie();
                }
            }
        });
    }

    private void displayUI(final Movie movie) {
        originalTitleTV.setText(movie.getOriginalTitle());

        Picasso
                .get()
                .load(Constants.POSTER_BASE_URL + movie.getPosterPath())
                .error(R.drawable.ic_android_black_24dp)
                .into(posterThumbnailIMV);

        userRatingTV.setText(movie.getHighestRated());
        releaseDateTV.setText(movie.getReleaseDate());
        movieOverviewTV.setText(movie.getPlotSynopsis());

        if(isFavorite) {
            favoriteButton.setText(R.string.delete_from_favorites);
        } else {
            favoriteButton.setText(R.string.add_to_favorites);
        }

        manageFavorites();
    }

    private void setAdapters() {
        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        trailerLV.setAdapter(mTrailerAdapter);
        mReviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        reviewLV.setAdapter(mReviewAdapter);

        trailerLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Trailer selectedTrailer = mTrailerAdapter.getItem(position);
                Uri trailerUri = Uri.parse(selectedTrailer.getTrailerUrl());

                Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, trailerUri);
                startActivity(youtubeIntent);
            }
        });
    }

    public LoaderManager.LoaderCallbacks<List<Trailer>> trailersLoader =
            new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @NonNull
        @Override
        public Loader<List<Trailer>> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new TrailerLoader(
                    DetailsActivity.this, QueryUtils.buildTrailerStringUrl(movieId));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Trailer>> loader, List<Trailer> trailers) {
            mTrailerAdapter.clear();
            if (trailers != null && !trailers.isEmpty()) {
                mTrailerAdapter.addAll(trailers);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Trailer>> loader) {

        }
    };

    public LoaderManager.LoaderCallbacks<List<Review>> reviewsLoader =
            new LoaderManager.LoaderCallbacks<List<Review>>() {
        @NonNull
        @Override
        public Loader<List<Review>> onCreateLoader(int i, @Nullable Bundle bundle) {
            return new ReviewLoader(
                    DetailsActivity.this, QueryUtils.buildReviewStringUrl(movieId));
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Review>> loader, List<Review> reviews) {
            mReviewAdapter.clear();
            if(reviews != null && !reviews.isEmpty()) {
                mReviewAdapter.addAll(reviews);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Review>> loader) {

        }
    };

}
