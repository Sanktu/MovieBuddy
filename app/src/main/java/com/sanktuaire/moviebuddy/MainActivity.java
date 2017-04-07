package com.sanktuaire.moviebuddy;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.sanktuaire.moviebuddy.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_tv)
    TextView mMainTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        NetworkUtils request = new NetworkUtils(mMainTv);
        request.execute(request.fetchMovieMostPopular());
    }
}
