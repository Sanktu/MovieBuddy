package com.sanktuaire.moviebuddy.data.movie;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sanktuaire.moviebuddy.data.review.ReviewContract;
import com.sanktuaire.moviebuddy.data.trailer.TrailerContract;

import java.util.ArrayList;

/**
 * Created by Sanktuaire on 29/09/2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 2;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieContract.MovieEntry.TABLE_NAME + " (" +

                        MovieContract.MovieEntry._ID                    + " INTEGER PRIMARY KEY, " +
                        MovieContract.MovieEntry.COLUMN_TITLE           + " TEXT NOT NULL, "       +
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE  + " TEXT NOT NULL,"        +
                        MovieContract.MovieEntry.COLUMN_OVERVIEW        + " TEXT NOT NULL, "       +
                        MovieContract.MovieEntry.COLUMN_RATING          + " REAL NOT NULL, "       +
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE    + " TEXT NOT NULL, "       +
                        MovieContract.MovieEntry.COLUMN_POSTER          + " TEXT, "       +
                        MovieContract.MovieEntry.COLUMN_BACKDROP        + " TEXT); ";


        final String SQL_CREATE_REVIEW_TABLE =

                "CREATE TABLE " + ReviewContract.ReviewEntry.TABLE_NAME + " (" +

                        ReviewContract.ReviewEntry._ID                  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ReviewContract.ReviewEntry.COLUMN_AUTHOR        + " TEXT NOT NULL, "                     +
                        ReviewContract.ReviewEntry.COLUMN_CONTENT       + " TEXT NOT NULL,"                      +
                        ReviewContract.ReviewEntry.COLUMN_EXCERPT       + " TEXT NOT NULL, "                     +
                        ReviewContract.ReviewEntry.COLUMN_URL           + " TEXT NOT NULL, "                     +
                        ReviewContract.ReviewEntry.COLUMN_MOVIE_ID      + " INTEGER NOT NULL, FOREIGN KEY("      +
                        ReviewContract.ReviewEntry.COLUMN_MOVIE_ID      + ") REFERENCES "                        +
                        MovieContract.MovieEntry.TABLE_NAME             + "("                                    +
                        MovieContract.MovieEntry._ID                    + "));";


        final String SQL_CREATE_TRAILER_TABLE =

                "CREATE TABLE " + TrailerContract.TrailerEntry.TABLE_NAME + " (" +

                        TrailerContract.TrailerEntry._ID                + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TrailerContract.TrailerEntry.COLUMN_NAME        + " TEXT NOT NULL, "                  +
                        TrailerContract.TrailerEntry.COLUMN_SIZE        + " TEXT NOT NULL,"                   +
                        TrailerContract.TrailerEntry.COLUMN_SOURCE      + " TEXT NOT NULL, "                     +
                        TrailerContract.TrailerEntry.COLUMN_TYPE        + " TEXT NOT NULL, "                     +
                        TrailerContract.TrailerEntry.COLUMN_MOVIE_ID    + " INTEGER NOT NULL, FOREIGN KEY("      +
                        TrailerContract.TrailerEntry.COLUMN_MOVIE_ID    + ") REFERENCES "                        +
                        MovieContract.MovieEntry.TABLE_NAME             + "("                                    +
                        MovieContract.MovieEntry._ID                    + "));";
        /*
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         */
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewContract.ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerContract.TrailerEntry.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }
}
