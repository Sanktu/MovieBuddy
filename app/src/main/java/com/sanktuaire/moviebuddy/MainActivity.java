package com.sanktuaire.moviebuddy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.data.movie.MovieAdapter;
import com.sanktuaire.moviebuddy.data.movie.Movies;
import com.sanktuaire.moviebuddy.utils.ItemOffsetDecoration;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private ArrayList<Movies>   movies;
    private MovieAdapter        mMovieAdapter;
    private Menu                mMenu;
    private boolean             mPopular;
    public static final int     nbPages = 3;

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

        // If Data already fetched, no need to do it again when we rotate or recreate activity
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(Intent.EXTRA_LOCAL_ONLY);
            mMovieAdapter.setMovieData(movies);
            mMovieAdapter.notifyDataSetChanged();
        } else
            loadMovies();
    }

    @Override
    public void onMovieClick(int clickedItem) {
        Intent intent = new Intent(this, DetailsActivity.class);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            Movies movie = movies.get(clickedItem);
            if (movie.isFav(this))
                movie.setFav(true);
            intent.putExtra(Intent.EXTRA_TEXT, movie);
            //intent.putParcelableArrayListExtra(Intent.EXTRA_LOCAL_ONLY, movies);
            startActivity(intent);
        }
    }

    private void loadMovies() {
        if (!isOnline()) {
            Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isPopular())
            new FetchTMDBTask().execute("popular");
        else
            new FetchTMDBTask().execute("top_rated");
    }

    private void loadFavMovies() {
        
        mRecyclerView.setVisibility(View.VISIBLE);
        mMovieAdapter.setMovieData(movies);
        updateMovies(movies);
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
        int idfav = R.id.favorite;


        if (id == idpop && !isPopular()) {
            setPopular(true);
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_off);
            loadMovies();
        }
        if (id == idrate && isPopular()) {
            setPopular(false);
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_off);
            loadMovies();
        }
        if (id == idfav) {
            mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_on);
            loadFavMovies();
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

        private ArrayList<Movies>   movies = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        protected ArrayList<Movies> doInBackground(String... params) {
            String[] jsonResults = new String[nbPages];
            JSONObject jsonObj[] = new JSONObject[nbPages];
            JSONArray pop[] = new JSONArray[nbPages];

            for (int i = 0; i < nbPages; i++) {
                jsonResults[i] = NetworkUtils.doTmdbQuery(params[0], String.valueOf(i + 1));
            }
            if (jsonResults[0] == null) {return null;}
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
                showErrorMessage();
            }
        }
    }

    private void showErrorMessage() {
        mProgressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, R.string.connectivity_problem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(Intent.EXTRA_LOCAL_ONLY, movies);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = savedInstanceState.getParcelableArrayList(Intent.EXTRA_LOCAL_ONLY);
        mMovieAdapter.setMovieData(movies);
        mMovieAdapter.notifyDataSetChanged();
    }
}
