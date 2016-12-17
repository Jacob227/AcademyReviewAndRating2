package com.example.academyreviewandrating;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by יעקב on 04/12/2016.
 */

public class NavigationFregmentRankAndReview extends Fragment {

    View myView;
    public NavigationFregmentRankAndReview() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.choose_rank_or_review,container,false);
        Log.d("onCreateView ","NavigationRankRev");
        return myView;
    }
}
