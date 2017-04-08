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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

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
        //        String[] mov = {movies.get(position).getTitle(),
//                movies.get(position).getOverview(),
//                movies.get(position).getPoster_path(),
//                movies.get(position).getBackdrop_path(),
//                movies.get(position).getRelease_date(),
//                movies.get(position).getVote_average().toString()};
        final String[] mov = intent.getStringArrayExtra(Intent.EXTRA_TEXT);
        mTitleDetails.setText(mov[0]);
        mReleaseDateDetails.setText(mov[4]);
        mOverviewDetails.setText(mov[1]);
        mAppCompatRatingBar.setRating(Float.parseFloat(mov[5])/2);
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p");
        if (dpi <= 320)
            uri.appendPath("w185");
        else
            uri.appendPath("w342");
        Picasso.with(this).load(uri.build().toString() + mov[2])
                .into(mPosterDetails);

        mAppCompatRatingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Toast.makeText(DetailsActivity.this, String.valueOf(mov[5]), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}
