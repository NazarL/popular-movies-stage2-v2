package com.example.popularmoviesv2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.UI.MovieAdapter;
import com.example.popularmoviesv2.UI.MovieLoader;
import com.example.popularmoviesv2.Utilities.QueryUtils;
import com.example.popularmoviesv2.ViewModel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private ProgressBar progressBar;
    private TextView showUserMessageTV;
    private RecyclerView mRecyclerView;

    private MovieAdapter movieAdapter;
    private MovieViewModel movieViewModel;

    String sortOptionSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.pgb_loading_spinner);
        showUserMessageTV = findViewById(R.id.txv_details_user_message);
        mRecyclerView = findViewById(R.id.rcv_posters_list);

        ArrayList<Movie> movieList = new ArrayList<>();
        movieAdapter = new MovieAdapter(this, movieList);

        mRecyclerView.setAdapter(movieAdapter);
        checkDisplayOrientation();
        mRecyclerView.setHasFixedSize(true);

        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        if (checkSortSelection().equals("favorites")) {
            displayFromDb();
        } else {
            displayFromQuery();
        }
    }

    public void displayFromQuery() {
        if (checkConnectivity() != null && checkConnectivity().isConnected()) {
            getSupportLoaderManager().initLoader(0, null, this);
        } else {
            showUserMessage(R.string.no_connection_message);
        }
    }

    public void displayFromDb() {
        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable final List<Movie> movies) {
                if (movies != null) {
                    movieAdapter.setData(movies);
                    progressBar.setVisibility(View.GONE);
                } else {
                    showUserMessage(R.string.nothing_found_message);
                }
            }
        });
    }

    public String checkSortSelection() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        sortOptionSelected = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );
        return sortOptionSelected;
    }

    private void showUserMessage(int message) {
        showUserMessageTV.setText(message);
        progressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void checkDisplayOrientation() {
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRecyclerView.setLayoutManager(
                    new GridLayoutManager(
                            this,
                            getResources().getInteger(R.integer.column_number_portrait)));
        } else {
            mRecyclerView.setLayoutManager(
                    new GridLayoutManager(
                            this,
                            getResources().getInteger(R.integer.column_number_landscape)));
        }
    }

    private NetworkInfo checkConnectivity() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.display_options:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.delete_all:
                Toast.makeText(getApplicationContext(),
                        "All Favorites have being deleted",
                        Toast.LENGTH_LONG).show();
                movieViewModel.deleteAllFavorites();
                return true;
            default:

                return super.onOptionsItemSelected(item);
    }



    }

    @NonNull
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, @Nullable Bundle bundle) {
        return new MovieLoader(this, QueryUtils.buildMovieStringUrl(sortOptionSelected));
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Movie>> loader, List<Movie> movies) {
        if (movies != null) {
            movieAdapter.setData(movies);
            progressBar.setVisibility(View.GONE);
        } else {
            showUserMessage(R.string.nothing_found_message);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Movie>> loader) {
        movieAdapter.setData(null);
    }
}