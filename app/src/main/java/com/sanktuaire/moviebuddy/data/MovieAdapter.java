package com.sanktuaire.moviebuddy.data;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanktuaire.moviebuddy.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private ArrayList<Movies>   mMovies;
    private Context             mContext;

    public MovieAdapter(Context context, ArrayList<Movies> movies) {
        mMovies = movies;
        mContext = context;
    }

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
        return mMovies.size();
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
