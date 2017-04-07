package com.sanktuaire.moviebuddy;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.sanktuaire.moviebuddy.data.MovieAdapter;
import com.sanktuaire.moviebuddy.data.Movies;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private ArrayList<Movies>   movies;
    private MovieAdapter        mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this);

        loadMovies();
    }

    private void loadMovies() {
        new FetchTMDBTask().execute("popular");
    }

    public class FetchTMDBTask extends AsyncTask<String, Void, ArrayList<Movies>> {

        private final String TAG = FetchTMDBTask.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {
            String jsonResults = NetworkUtils.doTmdbQuery(params[0]);
            Log.v(TAG, jsonResults);
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(movies);
            } else {
                //showErrorMessage();
            }
        }
    }
}
