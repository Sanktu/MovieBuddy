package com.sanktuaire.moviebuddy.data;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class Movie {
    private String  mOriginalTitle;
    private String  mOverview;
    private Float   mVoteAverage;
    private String  mPosterPath;
    private String  mBackdropPath;
    private String  mReleaseDate;

    public Movie(String originalTitle, String overview, Float voteAverage, String posterPath, String backdropPath, String releaseDate) {
        mOriginalTitle = originalTitle;
        mOverview = overview;
        mVoteAverage = voteAverage;
        mPosterPath = posterPath;
        mBackdropPath = backdropPath;
        mReleaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public Float getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Float voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        mBackdropPath = backdropPath;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }
}
