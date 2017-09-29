package com.sanktuaire.moviebuddy.fragmentDetailView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanktuaire.moviebuddy.DetailsActivity;
import com.sanktuaire.moviebuddy.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Sanktuaire on 27/09/2017.
 */

public class FragmentOverview extends Fragment {

    @BindView(R.id.overview_details)
    TextView overviewDetails;

    public FragmentOverview() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getArguments();

        overviewDetails.setText(bundle.getString(DetailsActivity.OVERVIEW));

        return rootView;
    }


}
