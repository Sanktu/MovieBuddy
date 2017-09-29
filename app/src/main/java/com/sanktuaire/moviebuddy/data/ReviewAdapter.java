package com.sanktuaire.moviebuddy.data;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sanktuaire.moviebuddy.R;

import java.util.List;

/**
 * Created by Sanktuaire on 28/09/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private List<Review>                mReviews;
    private final Context               mContext;
    private TextView                    tvContent;
    private TextView                    tvAuthor;
    final private ReviewClickListener   mOnClickListener;

    public interface ReviewClickListener {
        void onReviewClick(int clickIndex, View v);
    }

    public ReviewAdapter(Fragment fragment, ReviewClickListener listener) {
        mContext = fragment.getContext();
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review_list_item, parent, false);
        return new ReviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Review review = mReviews.get(position);


        tvAuthor.setText(review.getAuthor());
        if (position == 0) {
            tvContent.setText(review.getContent());
//            tvContent.setTag(R.id.ITEM_POSITION, position);
            tvContent.setTag(Boolean.TRUE);
        }
        else {
            tvContent.setText(review.getContent());
            tvContent.setTag(Boolean.FALSE);
        }

    }



    @Override
    public int getItemCount() {
        return (mReviews == null) ? 0 : mReviews.size();
    }

    public void setReviewsData(List<Review> reviews) {
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolder(View itemView) {
            super(itemView);

            tvAuthor = (TextView) itemView.findViewById(R.id.tv_review);
            tvContent = (TextView) itemView.findViewById(R.id.tv_review_content);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onReviewClick(clickedPosition, v);
//            Log.d("HAHAHAHAH", String.valueOf(clickedPosition));
//            if (tvContent.getTag() == Boolean.TRUE) {
//                tvContent.setText(mReviews.get(clickedPosition).getExcerpt());
//                tvContent.setTag(Boolean.FALSE);
//            } else {
//                tvContent.setText(mReviews.get(clickedPosition).getContent());
//                tvContent.setTag(Boolean.TRUE);
//            }
//            tvContent.setVisibility(View.INVISIBLE);
//            tvContent.setVisibility(View.VISIBLE);
        }

    }
}
