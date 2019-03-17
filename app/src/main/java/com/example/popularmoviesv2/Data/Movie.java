package com.example.popularmoviesv2.Data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.example.popularmoviesv2.Utilities.Constants;

@Entity(tableName = "MovieFavoritesTable")
public class Movie implements Parcelable {
    @NonNull
    @PrimaryKey
    private String id;
    private String posterPath;
    private String originalTitle;
    private String releaseDate;
    private String plotSynopsis;
    private String highestRated;

    public Movie(@NonNull String id, String posterPath, String originalTitle, String releaseDate,
                     String plotSynopsis, String highestRated) {
        this.id = id;
        this.posterPath = posterPath;
        this.originalTitle = originalTitle;
        this.releaseDate = releaseDate;
        this.plotSynopsis = plotSynopsis;
        this.highestRated = highestRated;
    }

    private Movie(Parcel in) {
        this.id = in.readString();
        this.posterPath = in.readString();
        this.originalTitle = in.readString();
        this.releaseDate = in.readString();
        this.plotSynopsis = in.readString();
        this.highestRated = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return this.id;
    }

    public String getPosterPath() {
        return this.posterPath;
    }

    public String getOriginalTitle() {
        return this.originalTitle;
    }

    public String getReleaseDate() {
        return this.releaseDate;
    }

    public String getPlotSynopsis() {
        return this.plotSynopsis;
    }

    public String getHighestRated() {
        return this.highestRated;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPlotSynopsis(String plotSynopsis) {
        this.plotSynopsis = plotSynopsis;
    }

    public void setHighestRated(String highestRated) {
        this.highestRated = highestRated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.posterPath);
        dest.writeString(this.originalTitle);
        dest.writeString(this.releaseDate);
        dest.writeString(this.plotSynopsis);
        dest.writeString(this.highestRated);
    }
}
