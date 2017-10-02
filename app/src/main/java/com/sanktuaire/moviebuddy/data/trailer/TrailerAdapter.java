package com.sanktuaire.moviebuddy.data.trailer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.fragmentDetailView.FragmentTrailer;


import java.util.List;


/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {
    private final static String YOUTUBE_LINK = "https://www.youtube.com/watch?v=";
    private List<Trailer>       mTrailers;
    private ImageButton         playButton;
    private TextView            trailer_name;
    private TextView            hd;

    public TrailerAdapter() {
    }


    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_trailer_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        final Trailer trailer = mTrailers.get(position);

        trailer_name.setText(trailer.getName());
        if (trailer.isHd())
            hd.setVisibility(View.VISIBLE);
        else
            hd.setVisibility(View.INVISIBLE);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_LINK + trailer.getSource()));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mTrailers == null) ? 0 : mTrailers.size();
    }

    public void setTrailerData(List<Trailer> trailers) {
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);

            playButton = (ImageButton) view.findViewById(R.id.playButton);
            trailer_name = (TextView) view.findViewById(R.id.tv_trailer_name);
            hd = (TextView) view.findViewById(R.id.tv_hd);
        }

    }
}
