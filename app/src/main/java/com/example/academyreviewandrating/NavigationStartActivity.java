package com.example.academyreviewandrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

public class NavigationStartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("NavigationItemSelected ","NavigationStartActivity class");
        android.app.FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_Rank_and_Review) {
            Log.d("NavigationItemSelected ","going to open Rank and review Fregment");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentRankAndReview()).commit();
        } else if (id == R.id.nav_Profile) {
            Log.d("NavigationItemSelected ","going to open NavigationFregmentProfile");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentProfile()).commit();
        } else if (id == R.id.nav_Send_messege) {
            Log.d("NavigationItemSelected ","going to open NavigationFregmentChat");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentChat()).commit();
        } else if (id == R.id.nav_Logout) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentLogout()).commit();
            Log.d("NavigationItemSelected ","Sign out and going to open LoginActivity");
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
        if (firebaseAuth.getCurrentUser() != null){
            Toast.makeText(this,"Logout",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
