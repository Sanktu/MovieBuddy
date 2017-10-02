package com.sanktuaire.moviebuddy.utils;

import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;

/**
 * Created by Sanktuaire on 02/10/2017.
 */

public interface AsyncTaskListener<T> {
    public void beforeTask();
    public void onTaskComplete(T result);
}
