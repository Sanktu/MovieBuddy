package com.sanktuaire.moviebuddy.data.movie;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanktuaire on 29/09/2017.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.sanktuaire.moviebuddy";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for MovieBuddy.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {
        /* The base CONTENT_URI used to query the Weather table from the content provider */
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_MOVIES)
                .build();


        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        /* Weather ID as returned by API, used to identify the icon to be used */
        public static final String COLUMN_OVERVIEW = "overview";

        /* Rating is stored as a float */
        public static final String COLUMN_RATING = "rating";

        /* Release date is stored as a string (year of release) */
        public static final String COLUMN_RELEASE_DATE = "release_date";

        /* Poster and backdrop will be stored as blob  */
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_BACKDROP = "backdrop";

    }
}
