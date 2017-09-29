package com.sanktuaire.moviebuddy.fragmentDetailView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.DetailsActivity;
import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.data.Review;
import com.sanktuaire.moviebuddy.data.ReviewAdapter;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class FragmentReview extends Fragment implements ReviewAdapter.ReviewClickListener {
    @BindView(R.id.recycler_view_fr)
    RecyclerView mRecyclerView;

    private ArrayList<Review>   mReviews;
    private ReviewAdapter       mReviewAdapter;
    private Context             mContext;
    private View                mLastView;

    public FragmentReview() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mReviewAdapter = new ReviewAdapter(this, this);
        mRecyclerView.setAdapter(mReviewAdapter);
        mLastView = null;

        Bundle bundle = getArguments();

        new FetchReviewTask().execute(bundle.getString(DetailsActivity.MOVIE_ID));

        return rootView;
    }

    private void updateReviews(ArrayList<Review> reviews) {
        this.mReviews = reviews;
    }

    @Override
    public void onReviewClick(int clickIndex, View v) {
        TextView tvContent = (TextView) v.findViewById(R.id.tv_review_content);
        if (tvContent.getTag() == Boolean.TRUE) {
            tvContent.setText(mReviews.get(clickIndex).getExcerpt());
            tvContent.setTag(Boolean.FALSE);
        } else {
            tvContent.setText(mReviews.get(clickIndex).getContent());
            tvContent.setTag(Boolean.TRUE);
        }

        if (mLastView != null && mLastView != v) {
            TextView tv = (TextView) mLastView.findViewById(R.id.tv_review_content);
            int position = mRecyclerView.getChildAdapterPosition(mLastView);
            if (tv.getTag() == Boolean.TRUE) {
                tv.setText(mReviews.get(position).getExcerpt());
                tv.setTag(Boolean.FALSE);
                tv.setVisibility(View.INVISIBLE);
                tv.setVisibility(View.VISIBLE);
            }
        }
        mLastView = v;
//        int nbItems = mRecyclerView.getAdapter().getItemCount();
//        for (int i = 0; i < nbItems; i++) {
//            TextView tv = (TextView) mRecyclerView.findViewHolderForAdapterPosition(i).itemView.findViewById(R.id.tv_review_content);
//            if (tv.getTag() == Boolean.TRUE && i != clickIndex) {
//                tv.setText(mReviews.get(i).getExcerpt());
//                tv.setTag(Boolean.FALSE);
//                tv.setVisibility(View.INVISIBLE);
//                tv.setVisibility(View.VISIBLE);
//                break;
//            }
//        }
        tvContent.setVisibility(View.INVISIBLE);
        tvContent.setVisibility(View.VISIBLE);
    }

    private class FetchReviewTask extends AsyncTask<String, Void, ArrayList<Review>> {

        private ArrayList<Review> mReviews = new ArrayList<>();
        private static final String REVIEWS = "reviews";

        @Override
        protected ArrayList<Review> doInBackground(String... params) {
            String jsonResult = NetworkUtils.doTmdbQuery(params[0], REVIEWS);
            try {
                JSONObject jsonObj = new JSONObject(jsonResult);
                JSONArray pop = jsonObj.getJSONArray("results");
                for (int i = 0; i < pop.length(); i++ ) {
                    Gson gson = new Gson();
                    Review review = gson.fromJson(pop.getJSONObject(i).toString(), Review.class);
                    mReviews.add(review);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mReviews;
        }

        @Override
        protected void onPostExecute(ArrayList<Review> reviews) {
            super.onPostExecute(reviews);
            if (reviews != null) {
                mReviewAdapter.setReviewsData(reviews);
                updateReviews(reviews);
            }
        }
    }


}
