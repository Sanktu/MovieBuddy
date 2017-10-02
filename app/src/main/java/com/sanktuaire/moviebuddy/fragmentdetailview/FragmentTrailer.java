package com.sanktuaire.moviebuddy.fragmentdetailview;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.DetailsActivity;
import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.data.movie.Movies;
import com.sanktuaire.moviebuddy.data.trailer.Trailer;
import com.sanktuaire.moviebuddy.data.trailer.TrailerAdapter;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class FragmentTrailer extends Fragment {

    @BindView(R.id.recycler_view_ft)
    RecyclerView mRecyclerView;

    private TrailerAdapter      mTrailerAdapter;
    private Movies              movie;
    public FetchTrailTask       mFetchTrailTask;

    public FragmentTrailer() {
        mFetchTrailTask = new FetchTrailTask();
        mTrailerAdapter = new TrailerAdapter();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mTrailerAdapter);

        if (savedInstanceState != null)
            movie = savedInstanceState.getParcelable(Intent.EXTRA_LOCAL_ONLY);
        else {
            Bundle bundle = getArguments();
            movie = bundle.getParcelable(DetailsActivity.MOVIE_BUNDLE);
        }

        if (movie != null)
            mTrailerAdapter.setTrailerData(movie.getTrailers());

        return rootView;
    }

    private void updateTrailers(Movies movie) {
        this.movie = movie;
    }


    public class FetchTrailTask extends AsyncTask<String, Void, Movies> {

        private static final String TRAILERS = "trailers";

        @Override
        protected Movies doInBackground(String... params) {
            String jsonResult = NetworkUtils.doTmdbQuery(params[0], TRAILERS);
            try {
                JSONObject jsonObj = new JSONObject(jsonResult);
                JSONArray pop = jsonObj.getJSONArray("youtube");
                for (int i = 0; i < pop.length(); i++) {
                    Gson gson = new Gson();
                    Trailer trailer = gson.fromJson(pop.getJSONObject(i).toString(), Trailer.class);
                    movie.addTrailer(trailer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return movie;
        }


        @Override
        protected void onPostExecute(Movies movie) {
            super.onPostExecute(movie);
            if (movie.getTrailers() != null) {
                mTrailerAdapter.setTrailerData(movie.getTrailers());
                updateTrailers(movie);
            }
        }
    }
    public void setMovie(Movies movie) {
        this.movie = movie;
        this.movie.initializeTrailers();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Intent.EXTRA_LOCAL_ONLY, movie);
    }

}
