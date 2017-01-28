package com.example.academyreviewandrating;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by יעקב on 04/12/2016.
 */

/**
 * Fragment logout class for safe logout from app
 * using firebaseAuth API
 */
public class NavigationFregmentLogout extends Fragment {
    private FirebaseAuth firebaseAuth;
    View myView;
    public NavigationFregmentLogout() {
    }

    /**
     * Logout from application
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();
        Log.d("onCreateView ","NavigationLogout");
        myView = inflater.inflate(R.layout.activity_login,container,false);
        getActivity().finish();
        return myView;
    }

}
