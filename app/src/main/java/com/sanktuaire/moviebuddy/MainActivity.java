package com.sanktuaire.moviebuddy;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.data.MovieAdapter;
import com.sanktuaire.moviebuddy.data.Movies;
import com.sanktuaire.moviebuddy.utils.ItemOffsetDecoration;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private ArrayList<Movies>   movies;
    private MovieAdapter        mMovieAdapter;
    private Toast               mToast;
    private Menu                mMenu;
    private boolean             mPopular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int columns;

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            columns = 4;
        else
            columns = 5;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this, this);
        mRecyclerView.setAdapter(mMovieAdapter);
        setPopular(true);

        loadMovies();
    }

    @Override
    public void onMovieClick(int clickedItem) {
        if (mToast != null) {mToast.cancel();}
        mToast = Toast.makeText(this, movies.get(clickedItem).getTitle(), Toast.LENGTH_SHORT);
        mToast.show();
        Intent intent = new Intent(this, DetailsActivity.class);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            String[] mov = {movies.get(clickedItem).getTitle(),
                    movies.get(clickedItem).getOverview(),
                    movies.get(clickedItem).getPoster_path(),
                    movies.get(clickedItem).getBackdrop_path(),
                    movies.get(clickedItem).getRelease_date(),
                    movies.get(clickedItem).getVote_average().toString()};
            intent.putExtra(Intent.EXTRA_TEXT, mov);
            startActivity(intent);
        }
    }

    private void loadMovies() {
        if (isPopular())
            new FetchTMDBTask().execute("popular");
        else
            new FetchTMDBTask().execute("toprated");
    }
    private void updateMovies(ArrayList<Movies> movies) {
        this.movies = movies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int idpop = R.id.most_popular_menu;
        int idrate = R.id.top_rated;


        if (id == idpop && !isPopular()) {
            Toast.makeText(this, "DO MOST POPULAR!", Toast.LENGTH_SHORT).show();
            setPopular(true);
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            loadMovies();
        }
        if (id == idrate && isPopular()) {
            Toast.makeText(this, "DO TOP RATED!", Toast.LENGTH_SHORT).show();
            setPopular(false);
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            loadMovies();
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isPopular() {
        return mPopular;
    }

    public void setPopular(boolean popular) {
        mPopular = popular;
    }

    public class FetchTMDBTask extends AsyncTask<String, Void, ArrayList<Movies>> {

        private final String TAG = FetchTMDBTask.class.getSimpleName();
        private ArrayList<Movies>   movies = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {
            int nbPages = 4;
            String[] jsonResults = new String[nbPages];
            JSONObject jsonObj[] = new JSONObject[nbPages];
            JSONArray pop[] = new JSONArray[nbPages];

            for (int i = 0; i < nbPages; i++) {
                jsonResults[i] = NetworkUtils.doTmdbQuery(params[0], String.valueOf(i + 1));
            }
            try {
                for (int i = 0; i < nbPages; i++) {
                    jsonObj[i] = new JSONObject(jsonResults[i]);
                    pop[i] = jsonObj[i].getJSONArray("results");
                    for (int j = 0; j < pop[i].length(); j++) {
                        Gson gson = new Gson();
                        Movies movie = gson.fromJson(pop[i].getJSONObject(j).toString(), Movies.class);
                        movies.add(movie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movies> movies) {
            super.onPostExecute(movies);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (movies != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(movies);
                updateMovies(movies);
            } else {
                //showErrorMessage();
            }
        }
    }
}
