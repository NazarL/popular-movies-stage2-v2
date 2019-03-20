package com.example.popularmoviesv2.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.Repository.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private final MovieRepository mRepository;
    private final LiveData<List<Movie>> mAllMovies;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MovieRepository(application);
        mAllMovies = mRepository.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }

    public LiveData<Movie> findMovieById(String id){
        return mRepository.findMovieById(id);
    }

    public void insertMovie(Movie movie) {
        mRepository.insertMovie(movie);
    }

    public void deleteAllFavorites() {
        mRepository.deleteAllFavorites();
    }

    public void deleteMovie(Movie movie) {
        mRepository.deleteMovie(movie);
    }
}
