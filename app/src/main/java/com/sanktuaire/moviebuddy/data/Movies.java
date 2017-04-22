package com.sanktuaire.moviebuddy.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class Movies implements Parcelable {

    private String  id;
    private String  original_title;
    private String  title;
    private String  overview;
    private Float   vote_average;
    private String  poster_path;
    private String  backdrop_path;
    private String  release_date;

    public Movies(String originalTitle, String title, String overview, Float voteAverage, String posterPath, String backdropPath, String releaseDate) {
        original_title = originalTitle;
        this.overview = overview;
        this.title = title;
        vote_average = voteAverage;
        poster_path = posterPath;
        backdrop_path = backdropPath;
        release_date = releaseDate;
    }


    protected Movies(Parcel in) {
        id = in.readString();
        original_title = in.readString();
        title = in.readString();
        overview = in.readString();
        vote_average = in.readFloat();
        poster_path = in.readString();
        backdrop_path = in.readString();
        release_date = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Float getVote_average() {
        return vote_average;
    }

    public void setVote_average(Float vote_average) {
        this.vote_average = vote_average;
    }

    public String getPoster_path() { return poster_path; }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }



    @Override
    public String toString() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(original_title);
        dest.writeString(title);
        dest.writeString(overview);
        dest.writeFloat(vote_average);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(release_date);
    }
}
