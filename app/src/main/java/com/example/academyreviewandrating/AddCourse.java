package com.example.academyreviewandrating;

import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class AddCourse extends Fragment {

    View myView;
    public AddCourse() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_add_course,container,false);
        Log.d("onCreateView ","NavigationSurvey");
        return myView;
    }
}
