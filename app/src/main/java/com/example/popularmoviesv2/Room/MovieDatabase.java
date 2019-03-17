package com.example.popularmoviesv2.Room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.popularmoviesv2.Data.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {


    private static final String LOG_TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favorite_movies";
    private static MovieDatabase INSTANCE;

    public abstract MovieDao movieDao();

    public static MovieDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                Log.d(LOG_TAG, "Creating new database instance");
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                        MovieDatabase.class,
                        MovieDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return INSTANCE;
    }
}
