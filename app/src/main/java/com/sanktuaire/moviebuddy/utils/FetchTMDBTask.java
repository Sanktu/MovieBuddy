package com.sanktuaire.moviebuddy.utils;

import android.os.AsyncTask;
import android.view.View;

import com.google.gson.Gson;
import com.sanktuaire.moviebuddy.data.movie.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sanktuaire on 02/10/2017.
 */

public class FetchTMDBTask extends AsyncTask<String, Void, ArrayList<Movies>> {

    private static final int                        NB_PAGES = 3;
    private ArrayList<Movies>                       movies;
    private AsyncTaskListener<ArrayList<Movies>>    listener;

    public FetchTMDBTask(AsyncTaskListener<ArrayList<Movies>> listener) {
        movies = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.beforeTask();
    }

    @Override
    protected ArrayList<Movies> doInBackground(String... params) {
        String[] jsonResults = new String[NB_PAGES];
        JSONObject jsonObj[] = new JSONObject[NB_PAGES];
        JSONArray pop[] = new JSONArray[NB_PAGES];

        for (int i = 0; i < NB_PAGES; i++) {
            jsonResults[i] = NetworkUtils.doTmdbQuery(params[0], String.valueOf(i + 1));
        }
        if (jsonResults[0] == null) {return null;}
        try {
            for (int i = 0; i < NB_PAGES; i++) {
                jsonObj[i] = new JSONObject(jsonResults[i]);
                pop[i] = jsonObj[i].getJSONArray("results");
                for (int j = 0; j < pop[i].length(); j++) {
                    Gson gson = new Gson();
                    Movies movie = gson.fromJson(pop[i].getJSONObject(j).toString(), Movies.class);
                    movies.add(movie);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movies> movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }
}