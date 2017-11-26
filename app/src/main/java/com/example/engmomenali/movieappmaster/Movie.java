package com.example.engmomenali.movieappmaster;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by MomenAli on 10/15/2017.
 */

public class Movie implements Parcelable {
    private long id;
    private String title;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private Double Ratings;
    private Double popularity;
    private String coverImagePath;


    public Movie(long id, String title, String overview, String releaseDate, String posterPath, Double ratings, Double popularity, String coverImagePath) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.coverImagePath = coverImagePath;
        Ratings = ratings;
        this.popularity = popularity;

    }

    public Movie() {

    }

    private Movie(Parcel in){
        this.id = in.readLong();
        this.title = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.coverImagePath = in.readString();
        Ratings = in.readDouble();
        this.popularity = in.readDouble();

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
         dest.writeLong(id);
         dest.writeString(title);
         dest.writeString(overview);
         dest.writeString(releaseDate);
         dest.writeString(posterPath);
         dest.writeString(coverImagePath);
         dest.writeDouble(Ratings);
         dest.writeDouble(popularity);
    }



    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[i];
        }

    };



    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setoverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setRatings(Double ratings) {
        Ratings = ratings;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public long getId() {

        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getoverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Double getRatings() {
        return Ratings;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }
}
