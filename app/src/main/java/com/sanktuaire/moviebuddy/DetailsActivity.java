package com.sanktuaire.moviebuddy;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanktuaire.moviebuddy.data.movie.MovieAdapter;
import com.sanktuaire.moviebuddy.data.movie.Movies;
import com.sanktuaire.moviebuddy.data.review.Review;
import com.sanktuaire.moviebuddy.data.review.ReviewContract;
import com.sanktuaire.moviebuddy.data.trailer.Trailer;
import com.sanktuaire.moviebuddy.data.trailer.TrailerContract;
import com.sanktuaire.moviebuddy.fragmentDetailView.FragmentOverview;
import com.sanktuaire.moviebuddy.fragmentDetailView.FragmentReview;
import com.sanktuaire.moviebuddy.fragmentDetailView.FragmentTrailer;
import com.sanktuaire.moviebuddy.fragmentDetailView.SectionsPageAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    public static final String  OVERVIEW = "overview";
    public static final String  MOVIE_ID = "movieID";
    public static final String  MOVIE_BUNDLE = "movieBundle";
    private Movies              movie;
    private boolean             isFavorite;

    @BindView(R.id.poster_details)
    ImageView mPosterDetails;
    @BindView(R.id.title_details)
    TextView mTitleDetails;
    @BindView(R.id.release_date_details)
    TextView mReleaseDateDetails;
    @BindView(R.id.appCompatRatingBar)
    AppCompatRatingBar mAppCompatRatingBar;
    @BindView(R.id.fav_star)
    ImageButton favorite;
    @BindView(R.id.constraint_layout)
    ConstraintLayout mConstraintLayout;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.pager)
    ViewPager mViewPager;
    @BindView(R.id.buttonDB)
    Button buttonDB;

    private SectionsPageAdapter mSectionsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable(Intent.EXTRA_LOCAL_ONLY);
        }




        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra(Intent.EXTRA_LOCAL_ONLY)) {
//            Toast.makeText(this, "WE GOT THE MOVIES?", Toast.LENGTH_SHORT).show();
//            movies = intent.getParcelableArrayListExtra(Intent.EXTRA_LOCAL_ONLY);
//        }

        if (intent == null || !intent.hasExtra(Intent.EXTRA_TEXT))
            mTitleDetails.setText(R.string.intent_error);
        else
            {
                final Movies mov = setupDetailActivity(intent, movie);
                mSectionsAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                setupViewPager(mViewPager, mSectionsAdapter, mov);
                mTabLayout.setupWithViewPager(mViewPager);
            }
    }

    @NonNull
    private Movies setupDetailActivity(Intent intent, Movies movie) {

        int dpi = getResources().getDisplayMetrics().densityDpi;
        final Movies mov;
        if (movie != null)
            mov = movie;
        else
            mov = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        mTitleDetails.setText(mov.getTitle());
        mReleaseDateDetails.setText(mov.getRelease_date().split("-")[0]);
        mAppCompatRatingBar.setRating(mov.getVote_average() / 2);

        /* DEBUG BUTTON FOR DB*/
        buttonDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dbmanager = new Intent(getApplicationContext(), AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

        if (mov.isFavorite()) {
            favorite.setImageResource(android.R.drawable.btn_star_big_on);
            favorite.setTag(Boolean.TRUE);
        } else {
            favorite.setTag(Boolean.FALSE);
        }
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite.getTag() == Boolean.FALSE) {
                    mov.setFavorite(getApplicationContext());
                    favorite.setImageResource(android.R.drawable.btn_star_big_on);
                    favorite.setTag(Boolean.TRUE);
                } else {
                    mov.removeFavorite(getApplicationContext());
                    favorite.setImageResource(android.R.drawable.btn_star_big_off);
                    favorite.setTag(Boolean.FALSE);
                }
            }
        });
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p");
        if (dpi <= 320)
            uri.appendPath("w185");
        else
            uri.appendPath("w342");
        Picasso.with(this).load(uri.build().toString() + mov.getPoster_path())
                .into(mPosterDetails);

        mAppCompatRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(DetailsActivity.this, String.valueOf(mov.getVote_average()), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        return mov;
    }


    private void setupViewPager(ViewPager viewPager, SectionsPageAdapter adapter, Movies movie) {
        Bundle bundle = new Bundle();
        bundle.putString(OVERVIEW, movie.getOverview());
//        bundle.putString(MOVIE_ID, movie.getId());
        bundle.putParcelable(MOVIE_BUNDLE, movie);

        FragmentOverview fragmentOverview = new FragmentOverview();
        FragmentReview fragmentReview = new FragmentReview();
        fragmentReview.setMovie(movie);
        FragmentTrailer fragmentTrailer = new FragmentTrailer();
        fragmentTrailer.setMovie(movie);

        if (movie.isFavorite()) {
            movie.setReviews(loadReviewsFromDB(movie.getId()));
            movie.setTrailers(loadTrailersFromDB(movie.getId()));
        } else {
            fragmentReview.mFetchReviewTask.execute(movie.getId());
            fragmentTrailer.mFetchTrailTask.execute(movie.getId());
        }

        fragmentOverview.setArguments(bundle);
        fragmentReview.setArguments(bundle);
        fragmentTrailer.setArguments(bundle);

        Resources res = getResources();
        adapter.addFragment(fragmentOverview, res.getString(R.string.overview_tab));
        adapter.addFragment(fragmentReview, res.getString(R.string.review_tab));
        adapter.addFragment(fragmentTrailer, res.getString(R.string.trailer_tab));

        viewPager.setAdapter(adapter);
    }

    private List<Review> loadReviewsFromDB(String movieID) {
        ContentResolver cr = getContentResolver();
        String[] mProjection =
                {
                        ReviewContract.ReviewEntry._ID,
                        ReviewContract.ReviewEntry.COLUMN_AUTHOR,
                        ReviewContract.ReviewEntry.COLUMN_CONTENT,
                        ReviewContract.ReviewEntry.COLUMN_URL
                };

        Cursor c = cr.query(ReviewContract.ReviewEntry.CONTENT_URI,
                mProjection,
                ReviewContract.ReviewEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{movieID},
                null);

        List<Review> reviews = new ArrayList<>();

        while (c.moveToNext()) {
            String id = c.getString(c.getColumnIndex(ReviewContract.ReviewEntry._ID));
            String author = c.getString(c.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_AUTHOR));
            String content = c.getString(c.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_CONTENT));
            String url = c.getString(c.getColumnIndex(ReviewContract.ReviewEntry.COLUMN_URL));

            reviews.add(new Review(id, author, content, url));
        }
        c.close();
        return reviews;
    }

    private List<Trailer> loadTrailersFromDB(String movieID) {
        ContentResolver cr = getContentResolver();
        String[] mProjection =
                {
                        TrailerContract.TrailerEntry.COLUMN_NAME,    // Contract class constant for the _ID column name
                        TrailerContract.TrailerEntry.COLUMN_SIZE,   // Contract class constant for the word column name
                        TrailerContract.TrailerEntry.COLUMN_SOURCE,
                        TrailerContract.TrailerEntry.COLUMN_TYPE
                };

        Cursor c = cr.query(TrailerContract.TrailerEntry.CONTENT_URI,
                mProjection,
                TrailerContract.TrailerEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[]{movieID},
                null);

        List<Trailer> trailers = new ArrayList<>();

        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_NAME));
            String size = c.getString(c.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_SIZE));
            String link = c.getString(c.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_SOURCE));
            String type = c.getString(c.getColumnIndex(TrailerContract.TrailerEntry.COLUMN_TYPE));

            trailers.add(new Trailer(name, size, link, type));
        }
        c.close();
        return trailers;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("???? => ", "DO WE GET THERE????");
        outState.putParcelable(Intent.EXTRA_LOCAL_ONLY, movie);
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}
