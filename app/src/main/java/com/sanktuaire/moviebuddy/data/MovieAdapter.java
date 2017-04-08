package com.sanktuaire.moviebuddy.data;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanktuaire.moviebuddy.MainActivity;
import com.sanktuaire.moviebuddy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private static final String         TAG = MovieAdapter.class.getSimpleName();
    private ArrayList<Movies>           mMovies;
    private Context                     mContext;
    final private MovieClickListener    mOnClickListener;

    public interface MovieClickListener {
        void onMovieClick(int clickIndex);
    }

    public MovieAdapter(MainActivity mainActivity, MovieClickListener listener) {
        mContext = mainActivity.getApplicationContext();
        mOnClickListener = listener;
    }

//    public MovieAdapter(Context context, ArrayList<Movies> movies) {
//        mMovies = movies;
//        mContext = context;
//    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movies movie = mMovies.get(position);
        int dpi = mContext.getResources().getDisplayMetrics().densityDpi;
        Uri.Builder uri = new Uri.Builder();
        uri.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p");
        if (dpi <= 320)
            uri.appendPath("w185");
        else
            uri.appendPath("w342");
        Picasso.with(mContext).load(uri.build().toString() + movie.getPoster_path()).into(holder.img_movie);
//        holder.tv_movie.setText(movie.getOriginal_title());
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    public void setMovieData(ArrayList<Movies> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        private TextView tv_movie;
        private ImageView img_movie;
        public ViewHolder(View view) {
            super(view);

            img_movie = (ImageView) view.findViewById(R.id.list_item_image);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onMovieClick(clickedPosition);
        }
    }
}
