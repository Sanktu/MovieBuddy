package com.sanktuaire.moviebuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sanktuaire.moviebuddy.data.Movies;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.poster_details)
    ImageView mPosterDetails;
    @BindView(R.id.title_details)
    TextView mTitleDetails;
    @BindView(R.id.release_date_details)
    TextView mReleaseDateDetails;
    @BindView(R.id.overview_details)
    TextView mOverviewDetails;
    @BindView(R.id.appCompatRatingBar)
    AppCompatRatingBar mAppCompatRatingBar;
    @BindView(R.id.constraint_layout)
    ConstraintLayout mConstraintLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        int dpi = getResources().getDisplayMetrics().densityDpi;

        Intent intent = getIntent();
        final Movies mov = intent.getParcelableExtra(Intent.EXTRA_TEXT);
        mTitleDetails.setText(mov.getTitle());
        mReleaseDateDetails.setText(mov.getRelease_date().split("-")[0]);
        mOverviewDetails.setText(mov.getOverview());
        mAppCompatRatingBar.setRating(mov.getVote_average()/2);
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
    }
}
