package com.sanktuaire.moviebuddy.utils;

import android.net.Uri;
import android.util.Log;

import com.sanktuaire.moviebuddy.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sanktuaire on 2017-04-07.
 */

public class NetworkUtils {
    private static final String TMDB_BASE_URL = "https://api.themoviedb.org";
    private static final String API_VERSION = "3";
    private static final String TAG = NetworkUtils.class.getSimpleName();



    public static URL buildUrl(String requestType, String page) {
        Uri.Builder buildUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath("movie");
        if (requestType.equals("popular"))
                buildUri.appendPath("popular");
        else
            buildUri.appendPath("top_rated");
        buildUri.appendQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
        .appendQueryParameter("page", page);
        Uri builtUri = buildUri.build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    public static String doTmdbQuery(String requestType, String page) {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        builder.url(buildUrl(requestType, page));
        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            int code = response.code();
            if (code != 200)
                return null;
            return response.body().string();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "NOPE! NO RESPONSE!");
        return null;
    }
}
