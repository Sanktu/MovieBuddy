package com.sanktuaire.moviebuddy.data.movie;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sanktuaire.moviebuddy.MainActivity;
import com.sanktuaire.moviebuddy.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private List<Movies>                mMovies;
    private Context                     mContext;
    final private MovieClickListener    mOnClickListener;
    private int                         width_pic;

    public interface MovieClickListener {
        void onMovieClick(int clickIndex);
    }

    public MovieAdapter(MainActivity mainActivity, MovieClickListener listener, int columns, int width) {
        mContext = mainActivity.getApplicationContext();
        mOnClickListener = listener;
        this.width_pic = width/columns;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        Movies movie = mMovies.get(position);

        if (movie.isFavorite()) {
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir("posters", Context.MODE_PRIVATE);
            File myImageFile = new File(directory, movie.getPoster_path().substring(1));
            Picasso.with(mContext).load(myImageFile)
                    .resize(width_pic, ((int) Math.round(width_pic * 1.5)))
                    .placeholder(R.drawable.movieplaceholder)
                    .error(R.drawable.movieplaceholder)
                    .into(holder.img_movie);
        } else {
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
            Picasso.with(mContext).load(uri.build().toString() + movie.getPoster_path())
                    .resize(width_pic, ((int) Math.round(width_pic * 1.5)))
                    .placeholder(R.drawable.movieplaceholder)
                    .error(R.drawable.movieplaceholder)
                    .into(holder.img_movie);
        }
    }

    @Override
    public int getItemCount() {
        return (mMovies == null) ? 0 : mMovies.size();
    }

    public void setMovieData(List<Movies> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
