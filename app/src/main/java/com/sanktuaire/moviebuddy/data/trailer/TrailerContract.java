package com.sanktuaire.moviebuddy.data.trailer;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Sanktuaire on 29/09/2017.
 */

public class TrailerContract {

    public static final String CONTENT_AUTHORITY = "com.sanktuaire.moviebuddy";

    /*
     * Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
     * the content provider for MovieBuddy.
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_TRAILERS = "trailers";

    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_TRAILERS)
                .build();


        /* Used internally as the name of our movies table. */
        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_SIZE = "size";
        public static final String COLUMN_SOURCE = "source";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_MOVIE_ID = "movieID";

    }
}
