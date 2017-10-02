package com.sanktuaire.moviebuddy;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sanktuaire.moviebuddy.data.movie.MovieAdapter;
import com.sanktuaire.moviebuddy.data.movie.MovieContract;
import com.sanktuaire.moviebuddy.data.movie.Movies;
import com.sanktuaire.moviebuddy.utils.AsyncTaskListener;
import com.sanktuaire.moviebuddy.utils.FetchTMDBTask;
import com.sanktuaire.moviebuddy.utils.ItemOffsetDecoration;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieClickListener{

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    private final String        POPULAR = "popular";
    private final String        TOP_RATED = "top_rated";
    private final String        FAVORITE = "favorite";
    private ArrayList<Movies>   movies;
    private MovieAdapter        mMovieAdapter;
    private Menu                mMenu;
    private boolean             mPopular;
    private String              mode;
    private String              oldMode;
    private int                 columns;
    private int                 width;
    public static final int     nbPages = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        columns = numberOfColumns();

//        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
//            columns = 4;
//        else
//            columns = 5;
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(layoutManager);
        mMovieAdapter = new MovieAdapter(this, this, columns, width);
        mRecyclerView.setAdapter(mMovieAdapter);
        mode = oldMode = POPULAR;

        // If Data already fetched, no need to do it again when we rotate or recreate activity
        if (savedInstanceState != null) {
            movies = savedInstanceState.getParcelableArrayList(Intent.EXTRA_LOCAL_ONLY);
            String savedMode = savedInstanceState.getString(Intent.CATEGORY_CAR_MODE);
            if (savedMode != null)
                mode = savedMode;
            mMovieAdapter.setMovieData(movies);
            mMovieAdapter.notifyDataSetChanged();
        } else
            loadMovies();
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = getResources().getDisplayMetrics().densityDpi;

        int widthDivider = (dpi <= 320) ? 185 : 342;
        width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 3;
        if (nColumns > 7) return 7;
        return nColumns;
    }

    @Override
    public void onMovieClick(int clickedItem) {
        Intent intent = new Intent(this, DetailsActivity.class);
        // Verify that the intent will resolve to an activity
        if (intent.resolveActivity(getPackageManager()) != null) {
            Movies movie = movies.get(clickedItem);
            if (movie.isFav(this))
                movie.setFav(true);
            if (isOnline() || movie.isFavorite()) {
                intent.putExtra(Intent.EXTRA_TEXT, movie);
                startActivity(intent);
            } else
                Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void loadMovies() {
        if (!isOnline()) {
            Toast.makeText(this, R.string.check_internet, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, R.string.switchfav, Toast.LENGTH_SHORT).show();
            mode = FAVORITE;
        }

        switch (mode) {
            case POPULAR:
                new FetchTMDBTask(new FetchMyDataTaskListener()).execute(POPULAR);
                break;
            case TOP_RATED:
                new FetchTMDBTask(new FetchMyDataTaskListener()).execute(TOP_RATED);
                break;
            case FAVORITE:
                loadFavMovies();
                break;
            default:
                new FetchTMDBTask(new FetchMyDataTaskListener()).execute(POPULAR);
        }
    }

    private void loadFavMovies() {

        mProgressBar.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        ContentResolver cr = getContentResolver();
        String[] mProjection =
                {
                        MovieContract.MovieEntry._ID,
                        MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                        MovieContract.MovieEntry.COLUMN_TITLE,
                        MovieContract.MovieEntry.COLUMN_OVERVIEW,
                        MovieContract.MovieEntry.COLUMN_RATING,
                        MovieContract.MovieEntry.COLUMN_POSTER,
                        MovieContract.MovieEntry.COLUMN_BACKDROP,
                        MovieContract.MovieEntry.COLUMN_RELEASE_DATE
                };

        Cursor c = cr.query(MovieContract.MovieEntry.CONTENT_URI,
                mProjection,
                null,
                null,
                null);

        movies = new ArrayList<>();

        if (c == null) return;

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(MovieContract.MovieEntry._ID));
            String originalTitle = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE));
            String title = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
            String overview = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
            String rating = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING));
            String posterPath = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER));
            String backdropPath = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP));
            String releaseDate = c.getString(c.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));

            Movies movie = new Movies(id, originalTitle, title, overview, Float.valueOf(rating), posterPath, backdropPath, releaseDate);
            movie.setFav(true);
            movies.add(movie);
        }

        c.close();
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mMovieAdapter.setMovieData(movies);
        mMovieAdapter.notifyDataSetChanged();
    }


    private void updateMovies(ArrayList<Movies> movies) {
        this.movies = movies;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        if (mode.equals(FAVORITE))
            mMenu.findItem(R.id.favorite).setIcon(android.R.drawable.btn_star_big_on);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int idpop = R.id.most_popular_menu;
        int idrate = R.id.top_rated;
        int idfav = R.id.favorite;

        if (id == idpop && !mode.equals(POPULAR)) {
            mode = oldMode = POPULAR;
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            if (isOnline())
                mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_off);
            else
                mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_on);
            loadMovies();
        }
        if (id == idrate && !mode.equals(TOP_RATED)) {
            mode = oldMode = TOP_RATED;
            mMenu.findItem(R.id.top_rated).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            mMenu.findItem(R.id.most_popular_menu).setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            if (isOnline())
                mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_off);
            else
                mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_on);
            loadMovies();
        }
        if (id == idfav && !mode.equals(FAVORITE)) {
            oldMode = mode;
            mode = FAVORITE;
            mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_on);
            loadMovies();
        } else if (id == idfav && mode.equals(FAVORITE)) {
            mode = oldMode;
            if (isOnline())
                mMenu.findItem(idfav).setIcon(android.R.drawable.btn_star_big_off);
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

    public class FetchMyDataTaskListener implements AsyncTaskListener<ArrayList<Movies>>
    {
        @Override
        public void beforeTask() {
            mProgressBar.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
        }

        @Override
        public void onTaskComplete(ArrayList<Movies> result) {
            mProgressBar.setVisibility(View.INVISIBLE);
            if (result != null) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mMovieAdapter.setMovieData(result);
                movies = result;
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
        outState.putString(Intent.CATEGORY_CAR_MODE, mode);
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        movies = savedInstanceState.getParcelableArrayList(Intent.EXTRA_LOCAL_ONLY);
        mode = savedInstanceState.getString(Intent.CATEGORY_CAR_MODE);
        mMovieAdapter.setMovieData(movies);
        mMovieAdapter.notifyDataSetChanged();
    }
}
