package com.example.android.popuvie.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.android.popuvie.model.Movie;

/**
 * Created by Asus X550ZE on 12/8/2016.
 */

public class MoviesDBHandler {
    private static final String TAG = "MovieTable";
    private MoviesDBHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    public MoviesDBHandler(Context ctx) {
        this.mCtx = ctx;
    }

    public MoviesDBHandler open() throws SQLException{
        Log.i(TAG, "OPening DataBase Connection....");
        this.mDbHelper = new MoviesDBHelper(mCtx);
        this.mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public long createMovie(int movieId, String movieTitle,String movieImg,String movieDesc,String movieRate,String movieDate,String movieGenres) {
        Log.i(TAG, "Inserting record...");
        ContentValues initialValues = new ContentValues();
        initialValues.put(MoviesContract.MovieEntry.COLUMN_MOVIE_ID, movieId);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_TITLE, movieTitle);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_IMG, movieImg);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_DESC, movieDesc);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_RATE, movieRate);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_DATE, movieDate);
        initialValues.put(MoviesContract.MovieEntry.COLUMN_GENRE, movieGenres);
        return mDb.insert(MoviesContract.MovieEntry.TABLE_MOVIES, null, initialValues);
    }

    public boolean deleteMovie(long rowId) {
        return mDb.delete(MoviesContract.MovieEntry.TABLE_MOVIES, MoviesContract.MovieEntry._ID + "=" + rowId, null) > 0;
    }

    public Cursor fetchAllMovie() {
        return mDb.query(MoviesContract.MovieEntry.TABLE_MOVIES, new String[] {MoviesContract.MovieEntry._ID,
                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                MoviesContract.MovieEntry.COLUMN_TITLE,
                MoviesContract.MovieEntry.COLUMN_IMG,
                MoviesContract.MovieEntry.COLUMN_DESC,
                MoviesContract.MovieEntry.COLUMN_RATE,
                MoviesContract.MovieEntry.COLUMN_DATE,
                MoviesContract.MovieEntry.COLUMN_GENRE}, null, null, null, null, null);
    }

    public Cursor fetchMovie(long empId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, MoviesContract.MovieEntry.TABLE_MOVIES, new String[] {MoviesContract.MovieEntry._ID,
                                MoviesContract.MovieEntry.COLUMN_MOVIE_ID,
                                MoviesContract.MovieEntry.COLUMN_TITLE,
                                MoviesContract.MovieEntry.COLUMN_IMG,
                                MoviesContract.MovieEntry.COLUMN_DESC,
                                MoviesContract.MovieEntry.COLUMN_RATE,
                                MoviesContract.MovieEntry.COLUMN_DATE,
                                MoviesContract.MovieEntry.COLUMN_GENRE}, MoviesContract.MovieEntry._ID + "=" + empId, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

}
