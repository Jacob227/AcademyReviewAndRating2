package com.example.academyreviewandrating;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.ChatMessage;
import com.example.academyreviewandrating.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NavigationStartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mListenDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDBref;
    private android.app.FragmentManager fragmentManager;
    public static TextView mTV = null;
    public static ImageView unreadM = null;
    public static ArrayList<String> UsernamesListUnRead =new ArrayList<String>();
    public static ArrayList<User> userList =new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        unreadM = (ImageView) findViewById(R.id.unread);
        toolbar.setTitle("      Academy R & R");
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View mView = navigationView.getHeaderView(0);
        mTV = (TextView) mView.findViewById(R.id.nav_user_name);
        mTV.setText("Academy Review and Rating");
        if (LoginActivity.user_ref != null){
            mTV.setText(LoginActivity.user_ref.getUserName() + "\n  " + LoginActivity.user_ref.getFaculty()
                    + ", " + LoginActivity.user_ref.getInstitution());
            Log.d("NavBar", "After mtv");
        }

        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentRankAndReview()).commit();

    mAuth = FirebaseAuth.getInstance();
    mListenDatabase = FirebaseDatabase.getInstance().getReference("Messeges").child(mAuth.getCurrentUser().getUid());
    mListenDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Toast.makeText(NavigationStartActivity.this,"hiiiiiiii",Toast.LENGTH_LONG).show();
            for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                for(DataSnapshot mDataSnapshot1: mDataSnapshot.getChildren()){
                    ChatMessage unreadMessege = mDataSnapshot1.getValue(ChatMessage.class);
                    if (unreadMessege.getread() == false){
                        unreadM.setVisibility(ImageView.VISIBLE);
                        UsernamesListUnRead.add(unreadMessege.getMessageUser());
                    }
                }
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
        mListenDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mListenDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                        User UserS1 = mDataSnapshot.getValue(User.class);
                        if (userList.contains(UserS1) == false){
                            userList.add(UserS1);
                        }
                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Boolean setUser = false;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!setUser) {
            if (LoginActivity.user_ref != null) {
                Log.d("NavBar", "notNUll");
                TextView mTV = (TextView) findViewById(R.id.nav_user_name);
                mTV.setText(LoginActivity.user_ref.getUserName() + "\n  " + LoginActivity.user_ref.getFaculty()
                        + ", " + LoginActivity.user_ref.getInstitution());
                setUser = true;
            }
        }
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
     //   getMenuInflater().inflate(R.menu.navigation_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

 //       //noinspection SimplifiableIfStatement
 //       if (id == R.id.action_settings) {
 //           return true;
 //       }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Log.d("NavigationItemSelected ","NavigationStartActivity class");
        fragmentManager = getFragmentManager();

        if (id == R.id.nav_Rank_and_Review) {
            Log.d("NavigationItemSelected ","going to open Rank and review Fregment");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentRankAndReview()).commit();
        } else if (id == R.id.nav_Profile) {
            Log.d("NavigationItemSelected ","going to open NavigationFregmentProfile");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentProfile()).commit();
        } else if (id == R.id.nav_Send_messege) {
            Log.d("NavigationItemSelected ","going to open NavigationFregmentChat");
            fragmentManager.beginTransaction().replace(R.id.content_frame,new ChatRoom()).commit();
        } else if (id == R.id.nav_Logout) {
            //fragmentManager.beginTransaction().replace(R.id.content_frame,new NavigationFregmentLogout()).commit();
            Log.d("NavigationItemSelected ","Sign out and going to open LoginActivity");
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        } else if (id == R.id.nav_my_courses) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new ChooseScheduleFragment()).commit();
        } else if (id == R.id.nav_add_course) {
            if (LoginActivity.user_ref != null){
                if (LoginActivity.user_ref.isPrivilage().equals("Student")){
                    Toast.makeText(this,"You are not allowed to insert new course",Toast.LENGTH_LONG).show();
                }
                else {
                    fragmentManager.beginTransaction().replace(R.id.content_frame,new AddCourse()).commit();
                }
            }
         } else if (id == R.id.nav_AffiliateToSystem) {
            fragmentManager.beginTransaction().replace(R.id.content_frame,new AffiliateToSystem()).commit();
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
