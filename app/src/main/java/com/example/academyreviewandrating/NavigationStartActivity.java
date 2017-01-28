package com.example.academyreviewandrating;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Navigation Start Activity calls after login activity
 * Control the Drawer navigation
 */
public class NavigationStartActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    static Map<String, String> mMap = new HashMap<String, String>();
    private static int oneTimeInitHashMapUser = 0;
    private FirebaseAuth mAuth;
    private DatabaseReference mListenDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDBref;
    private android.app.FragmentManager fragmentManager;
    public static TextView mTV = null;
    public static ImageView unreadM = null;
    public static ArrayList<String> UsernamesListUnRead =new ArrayList<String>();
    public static ArrayList<User> userList =new ArrayList<User>();
    static ProgressBar mPB;
    private Activity myAct;

    /**
     * Init firebase and navigationView instances
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        myAct = this;
        mPB = (ProgressBar)findViewById(R.id.progressBar2);
        mPB.setVisibility(View.VISIBLE);
        mPB.getIndeterminateDrawable().setColorFilter(0x90000000, android.graphics.PorterDuff.Mode.MULTIPLY);

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
            int on=0,counnt = 0;
            for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                String Se = "",Re = "";
                for(DataSnapshot mDataSnapshot1: mDataSnapshot.getChildren()){
                    ChatMessage unreadMessege = mDataSnapshot1.getValue(ChatMessage.class);
                    //save the sender UserName
                    if (Se.isEmpty() == true){
                        Se = unreadMessege.getMessageUser();
                    }
                    //save the Reciver UserName
                    if((Se.equals(unreadMessege.getMessageUser())==false) &&( Re.isEmpty() ==true)){
                        Re = unreadMessege.getMessageUser();
                    }
                    //check if all the messages were readed
                    if (unreadMessege.getread() == false){
                        counnt++;
                        Toast.makeText(NavigationStartActivity.this,"You have got a message!!!",Toast.LENGTH_LONG).show();
                        unreadM.clearAnimation();
                        unreadM.requestFocus();
                        unreadM.setVisibility(ImageView.VISIBLE);
                        try {
                            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                            r.play();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                      // if found message that not readed add to arraylist this user name
                        if(UsernamesListUnRead.contains(unreadMessege.getMessageUser()) == false) {
                            UsernamesListUnRead.add(unreadMessege.getMessageUser());
                        }
                    }
                    //check if the message is already readed
                    if (counnt==0 && UsernamesListUnRead.isEmpty() == false){
                        if (UsernamesListUnRead.contains(Re)== true){
                            UsernamesListUnRead.remove(Re);
                        }
                        if (UsernamesListUnRead.contains(Se)== true){
                            UsernamesListUnRead.remove(Se);
                        }
                    }
                    //check if all the messages were readed
                    if(counnt== 0 && UsernamesListUnRead.isEmpty() ==true && on==0){
                        unreadM.setVisibility(ImageView.INVISIBLE);
                        on=1;
                    }
                }
            }
            NavigationStartActivity.mPB.setVisibility(View.INVISIBLE);
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
                if (oneTimeInitHashMapUser == 0)
                {
                    for(DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                        String us =  mDataSnapshot.getValue(User.class).getUserName();
                        String KeyUid = mDataSnapshot.getKey();
                        mMap.put(us,KeyUid);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Boolean setUser = false;

    /**
     * Override onBackPressed show dialog bar when you press on back button
     */
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
            //super.onBackPressed();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Confirm");
            builder.setMessage("Do you want to exit?");


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing but close the dialog
                    finish();
                    if (firebaseAuth.getCurrentUser() != null){
                        Toast.makeText(myAct,"Logout",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                    }
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // Do nothing
                    dialog.dismiss();
                }
            });

            AlertDialog alert = builder.create();
            alert.show();
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

    /**
     * Navigate to user selection on drawer navigation bar
     * @param item
     * @return
     */
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
            Bundle bundle = new Bundle();
            bundle.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            bundle.putSerializable("userClass",LoginActivity.user_ref);
            NavigationFregmentProfile FragmentProfileInstance = new NavigationFregmentProfile();
            FragmentProfileInstance.setArguments(bundle);
            fragmentManager.beginTransaction().replace(R.id.content_frame,FragmentProfileInstance).commit();
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
         } else if (id == R.id.nav_Class_servey){
            if (LoginActivity.user_ref != null) {
                if (LoginActivity.user_ref.isPrivilage().equals("Teacher")) {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ClassServeyLec()).commit();
                } else {
                    fragmentManager.beginTransaction().replace(R.id.content_frame, new ClassServeyStud()).commit();
                }
            }
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
}
