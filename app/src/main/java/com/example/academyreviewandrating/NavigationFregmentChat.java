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

public class NavigationFregmentChat extends Fragment {

    View myView;
    public NavigationFregmentChat() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.chat_activity,container,false);
        Log.d("onCreateView ","NavigationChat");
        return myView;
    }
}
