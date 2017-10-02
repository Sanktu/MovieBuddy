package com.sanktuaire.moviebuddy.utils;

/**
 * Created by Sanktuaire on 02/10/2017.
 * Interface MainActivity to FetchTMDBTask class
 */

public interface AsyncTaskListener<T> {
    void beforeTask();
    void onTaskComplete(T result);
}
