package com.example.popularmoviesv2.Room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.popularmoviesv2.Data.Movie;

import java.util.List;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM MovieFavoritesTable ORDER BY id")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM MovieFavoritesTable WHERE id=:id")
    LiveData<Movie> findMovieById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("DELETE FROM MovieFavoritesTable")
    void deleteAllFavorites();
}
