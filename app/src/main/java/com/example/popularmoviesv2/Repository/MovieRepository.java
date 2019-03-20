package com.example.popularmoviesv2.Repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.popularmoviesv2.Data.Movie;
import com.example.popularmoviesv2.Room.MovieDao;
import com.example.popularmoviesv2.Room.MovieDatabase;

import java.util.List;

public class MovieRepository {

    private final MovieDao mMovieDao;
    private final LiveData<List<Movie>> mAllMovies;

    public MovieRepository(Application application) {
        MovieDatabase db = MovieDatabase.getDatabase(application);
        mMovieDao = db.movieDao();
        mAllMovies = mMovieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies(){
        return mAllMovies;
    }

    public LiveData<Movie> findMovieById(String id){
        return (LiveData<Movie>) mMovieDao.findMovieById(id);
    }

    public void insertMovie(Movie movie) {
        new insertAsyncTask(mMovieDao).execute(movie);
    }

    public void deleteAllFavorites() {
        new deleteAllAsyncTask(mMovieDao).execute();
    }

    public void deleteMovie(Movie movie) {
        new deleteMovieAsyncTask(mMovieDao).execute(movie);
    }

    private static class insertAsyncTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mAsyncTaskDao;

        insertAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Movie... movies) {
            mAsyncTaskDao.insertMovie(movies[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private final MovieDao mAsyncTaskDao;

        deleteAllAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAllFavorites();
            return null;
        }
    }

    private static class deleteMovieAsyncTask extends AsyncTask<Movie, Void, Void> {
        private final MovieDao mAsyncTaskDao;

        deleteMovieAsyncTask(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            mAsyncTaskDao.deleteMovie(movies[0]);
            return null;
        }
    }

}
