package com.sanktuaire.moviebuddy.data;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class Movies {
    private String  original_title;
    private String  overview;
    private Float   vote_average;
    private String  poster_path;
    private String  backdrop_path;
    private String  release_date;

    public Movies(String originalTitle, String overview, Float voteAverage, String posterPath, String backdropPath, String releaseDate) {
        original_title = originalTitle;
        this.overview = overview;
        vote_average = voteAverage;
        poster_path = posterPath;
        backdrop_path = backdropPath;
        release_date = releaseDate;
    }

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

    public String getPoster_path() {
        return "http://image.tmdb.org/t/p/w185" + poster_path;
    }

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
}
