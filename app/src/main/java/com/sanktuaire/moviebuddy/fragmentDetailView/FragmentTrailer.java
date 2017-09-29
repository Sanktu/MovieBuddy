package com.sanktuaire.moviebuddy.fragmentDetailView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.DetailsActivity;
import com.sanktuaire.moviebuddy.R;
import com.sanktuaire.moviebuddy.data.Trailer;
import com.sanktuaire.moviebuddy.data.TrailerAdapter;
import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class FragmentTrailer extends Fragment {

    @BindView(R.id.recycler_view_ft)
    RecyclerView mRecyclerView;

    private ArrayList<Trailer> mTrailers;
    private TrailerAdapter mTrailerAdapter;
    private Context mContext;

    public FragmentTrailer() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_trailers, container, false);
        ButterKnife.bind(this, rootView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerView.setAdapter(mTrailerAdapter);

        Bundle bundle = getArguments();

        new FetchTrailTask().execute(bundle.getString(DetailsActivity.MOVIE_ID));

        return rootView;
    }

    private void updateTrailers(ArrayList<Trailer> trailers) {
        this.mTrailers = trailers;
    }

    class FetchTrailTask extends AsyncTask<String, Void, ArrayList<Trailer>> {

        private ArrayList<Trailer> mTrailers = new ArrayList<>();
        private static final String TRAILERS = "trailers";

        @Override
        protected ArrayList<Trailer> doInBackground(String... params) {
            String jsonResult = NetworkUtils.doTmdbQuery(params[0], TRAILERS);
            try {
                JSONObject jsonObj = new JSONObject(jsonResult);
                JSONArray pop = jsonObj.getJSONArray("youtube");
                for (int i = 0; i < pop.length(); i++ ) {
                    Gson gson = new Gson();
                    Trailer trailer = gson.fromJson(pop.getJSONObject(i).toString(), Trailer.class);
                    mTrailers.add(trailer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mTrailers;
        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> trailers) {
            super.onPostExecute(trailers);
            if (trailers != null) {
                mTrailerAdapter.setTrailerData(trailers);
                updateTrailers(trailers);
            }
        }
    }

}
