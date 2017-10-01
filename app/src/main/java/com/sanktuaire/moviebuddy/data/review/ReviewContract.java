package com.sanktuaire.moviebuddy.data.review;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanktuaire on 29/09/2017.
 */

public class ReviewContract {

    public static final String CONTENT_AUTHORITY = "com.sanktuaire.moviebuddy";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for MovieBuddy.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_REVIEWS = "reviews";

    public static final class ReviewEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_REVIEWS)
                .build();


        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_AUTHOR = "author";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_EXCERPT = "excerpt";
        public static final String COLUMN_MOVIE_ID = "movieID";

    }
}
