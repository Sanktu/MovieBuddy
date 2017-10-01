package com.sanktuaire.moviebuddy.fragmentDetailView;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.DetailsActivity;
import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.data.movie.MovieAdapter;
import com.sanktuaire.moviebuddy.data.movie.MovieContract;
import com.sanktuaire.moviebuddy.data.movie.Movies;
import com.sanktuaire.moviebuddy.data.review.Review;
import com.sanktuaire.moviebuddy.data.review.ReviewAdapter;
import com.sanktuaire.moviebuddy.data.review.ReviewContract;
import com.sanktuaire.moviebuddy.data.trailer.Trailer;
import com.sanktuaire.moviebuddy.data.trailer.TrailerContract;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class FragmentReview extends Fragment implements ReviewAdapter.ReviewClickListener {
    @BindView(R.id.recycler_view_fr)
    RecyclerView mRecyclerView;

    //private ArrayList<Review>   mReviews;
    private ReviewAdapter       mReviewAdapter;
    private Context             mContext;
    private View                mLastView;
    private Movies              movie;
    public FetchReviewTask      mFetchReviewTask;

    public FragmentReview() {
        mFetchReviewTask = new FetchReviewTask();
        mReviewAdapter = new ReviewAdapter(this, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mReviewAdapter);
        mLastView = null;

        if (savedInstanceState != null)
            movie = savedInstanceState.getParcelable(Intent.EXTRA_LOCAL_ONLY);
        else {
            Bundle bundle = getArguments();
            movie = bundle.getParcelable(DetailsActivity.MOVIE_BUNDLE);
        }

        mReviewAdapter.setReviewsData(movie.getReviews());
//        if (movie.getReviews().size() == 0)
//            new FetchReviewTask().execute(movie.getId());
//        else {
//            mReviewAdapter.setReviewsData(movie.getReviews());
//        }

        return rootView;
    }

    private void updateReviews(Movies movie) {
        this.movie = movie;
    }

    @Override
    public void onReviewClick(int clickIndex, View v) {
        if (mRecyclerView.getChildAdapterPosition(v) == mRecyclerView.getAdapter().getItemCount() - 1) {
            Log.d("======> ", "LASTO LASTO LASTO");
            return;
        }
        TextView tvContent = (TextView) v.findViewById(R.id.tv_review_content);
        if (tvContent.getTag() == Boolean.TRUE) {
            tvContent.setText(movie.getReviews().get(clickIndex).getExcerpt());
            tvContent.setTag(Boolean.FALSE);
        } else {
            tvContent.setText(movie.getReviews().get(clickIndex).getContent());
            tvContent.setTag(Boolean.TRUE);
        }

        if (mLastView != null && mLastView != v) {
            TextView tv = (TextView) mLastView.findViewById(R.id.tv_review_content);
            int position = mRecyclerView.getChildAdapterPosition(mLastView);
            if (tv.getTag() == Boolean.TRUE) {
                tv.setText(movie.getReviews().get(position).getExcerpt());
                tv.setTag(Boolean.FALSE);
                tv.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.VISIBLE);
            }
        }
        mLastView = v;

        tvContent.setVisibility(View.INVISIBLE);
        tvContent.setVisibility(View.VISIBLE);
    }



    public class FetchReviewTask extends AsyncTask<String, Void, Movies> {

        //private ArrayList<Review> mReviews = new ArrayList<>();
        private static final String REVIEWS = "reviews";

        @Override
        protected Movies doInBackground(String... params) {

            String jsonResult = NetworkUtils.doTmdbQuery(params[0], REVIEWS);
            try {
                JSONObject jsonObj = new JSONObject(jsonResult);
                JSONArray pop = jsonObj.getJSONArray("results");
                for (int i = 0; i < pop.length(); i++) {
                    Gson gson = new Gson();
                    Review review = gson.fromJson(pop.getJSONObject(i).toString(), Review.class);
                    movie.addReview(review);
                    //mReviews.add(review);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }


        @Override
        protected void onPostExecute(Movies movie) {
            super.onPostExecute(movie);
            if (movie.getReviews() != null) {
                mReviewAdapter.setReviewsData(movie.getReviews());
                updateReviews(movie);
            }
        }
    }

    public void setMovie(Movies movie) {
        this.movie = movie;
        this.movie.initializeReviews();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Intent.EXTRA_LOCAL_ONLY, movie);
    }
}
