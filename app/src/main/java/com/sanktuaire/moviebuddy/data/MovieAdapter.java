package com.sanktuaire.moviebuddy.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
    private ArrayList<Movies>   mMovies;
    private Context             mContext;

    public MovieAdapter(MainActivity mainActivity) {
        mContext = mainActivity.getApplicationContext();
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
        Picasso.with(mContext).load(movie.getPosterPath()).into(holder.img_movie);
        holder.tv_movie.setText(movie.getOriginalTitle());
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    public void setMovieData(ArrayList<Movies> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_movie;
        private ImageView img_movie;
        public ViewHolder(View view) {
            super(view);

            tv_movie = (TextView)view.findViewById(R.id.list_item_name);
            img_movie = (ImageView) view.findViewById(R.id.list_item_image);
        }
    }
}
