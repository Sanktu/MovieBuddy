package com.sanktuaire.moviebuddy.data.movie;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.data.review.Review;
import com.sanktuaire.moviebuddy.data.review.ReviewContract;
import com.sanktuaire.moviebuddy.data.trailer.Trailer;
import com.sanktuaire.moviebuddy.data.trailer.TrailerContract;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class Movies implements Parcelable {

    private String              id;
    private String              original_title;
    private String              title;
    private String              overview;
    private Float               vote_average;
    private String              poster_path;
    private String              backdrop_path;
    private String              release_date;
    private boolean             isFavorite;
    private List<Review>        mReviews;
    private List<Trailer>       mTrailers;


    public Movies(String id, String originalTitle, String title, String overview, Float voteAverage, String posterPath, String backdropPath, String releaseDate) {
        this.id = id;
        original_title = originalTitle;
        this.overview = overview;
        this.title = title;
        vote_average = voteAverage;
        poster_path = posterPath;
        backdrop_path = backdropPath;
        release_date = releaseDate;
        isFavorite = false;
        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
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
        isFavorite = in.readByte() != 0;
        mReviews = new ArrayList<>();
        mTrailers = new ArrayList<>();
        in.readList(mReviews, Review.class.getClassLoader());
        in.readList(mTrailers, Trailer.class.getClassLoader());
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

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFav(boolean favorite) {
        isFavorite = favorite;
    }

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
        dest.writeByte((byte) (isFavorite ? 1 : 0));     //if myBoolean == true, byte == 1
        dest.writeList(mReviews);
        dest.writeList(mTrailers);
    }

    public void setFavorite(final Context context) {

        ContentResolver cr = context.getContentResolver();
        ContentValues contentValues = new ContentValues();

        int dpi = context.getResources().getDisplayMetrics().densityDpi;
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p");
        if (dpi <= 320)
            uri.appendPath("w185");
        else
            uri.appendPath("w342");

        String url = uri.build().toString() + poster_path;

        Picasso.with(context)
                .load(url)
                .into(picassoImageTarget(context, "posters", poster_path.substring(1)));

        contentValues.put(MovieContract.MovieEntry._ID, id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, original_title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, vote_average);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, original_title);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER, poster_path);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, release_date);

        Toast.makeText(context, "Favorite " + title, Toast.LENGTH_SHORT).show();

        // Insert the content values via a ContentResolver
        cr.insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);

        for (Review review : mReviews) {
            ContentValues cv = new ContentValues();
            cv.put(ReviewContract.ReviewEntry.COLUMN_AUTHOR, review.getAuthor());
            cv.put(ReviewContract.ReviewEntry.COLUMN_CONTENT, review.getContent());
            cv.put(ReviewContract.ReviewEntry.COLUMN_URL, review.getUrl());
            cv.put(ReviewContract.ReviewEntry.COLUMN_EXCERPT, review.getExcerpt());
            cv.put(ReviewContract.ReviewEntry.COLUMN_MOVIE_ID, id);

            cr.insert(ReviewContract.ReviewEntry.CONTENT_URI, cv);
        }

        for (Trailer trailer : mTrailers) {
            ContentValues cv = new ContentValues();
            cv.put(TrailerContract.TrailerEntry.COLUMN_NAME, trailer.getName());
            cv.put(TrailerContract.TrailerEntry.COLUMN_SIZE, trailer.getSize());
            cv.put(TrailerContract.TrailerEntry.COLUMN_SOURCE, trailer.getSource());
            cv.put(TrailerContract.TrailerEntry.COLUMN_TYPE, trailer.getType());
            cv.put(TrailerContract.TrailerEntry.COLUMN_MOVIE_ID, id);

            cr.insert(TrailerContract.TrailerEntry.CONTENT_URI, cv);
        }
        isFavorite = true;
    }

    public void removeFavorite(Context context) {
        ContentResolver cr = context.getContentResolver();

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        cr.delete(uri, null, null);
        isFavorite = false;
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("posters", Context.MODE_PRIVATE);
        File myImageFile = new File(directory, poster_path.substring(1));
        if (myImageFile.delete()) Log.d("removeFavorite", "image on the disk deleted successfully!");
        Toast.makeText(context, "Unfavorite " + title, Toast.LENGTH_SHORT).show();
    }

    public void initializeReviews() {
        if (mReviews == null)
            mReviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        mReviews.add(review);
    }

    public List<Review> getReviews() {
        return mReviews;
    }

    public void addTrailer(Trailer trailer) {
        mTrailers.add(trailer);
    }
    public List<Trailer> getTrailers() {
        return mTrailers;
    }

    public void initializeTrailers() {
        if (mTrailers == null)
            mTrailers = new ArrayList<>();
    }

    public void setTrailers(List<Trailer> trailers) {
        mTrailers = trailers;
    }

    public void setReviews(List<Review> reviews) {
        mReviews = reviews;
    }

    public boolean isFav(Context context) {
        ContentResolver cr = context.getContentResolver();


        Cursor c = cr.query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry._ID},
                MovieContract.MovieEntry._ID + " = ? ",
                new String[]{id},
                null);

        if (c == null) return false;
        if (c.getCount() != 0) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

    private Target picassoImageTarget(Context context, final String imageDir, final String imageName) {

        ContextWrapper cw = new ContextWrapper(context);
        final File directory = cw.getDir(imageDir, Context.MODE_PRIVATE); // path to /data/data/yourapp/app_imageDir
        return new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File myImageFile = new File(directory, imageName); // Create image file
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(myImageFile);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }
            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if(placeHolderDrawable != null) {}
            }
        };
    }

}
