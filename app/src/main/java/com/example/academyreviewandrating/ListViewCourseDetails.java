package com.example.academyreviewandrating;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListViewCourseDetails extends Activity {

    private ListView listView;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceDelAdd;
    private Toolbar myActionBar;
    private TextView textViewTitle;
    private String[] intendMes;
    private FirebaseAuth firebaseAuth;
    private CourseDetailsModel courseDetailsModel;
    private FirebaseUser firebaseUser;
    private boolean registeredToCourse;
    private HashMap<String, ArrayList<rating_lecterer_model>> hashMap_rating;
    String[] items= {"Watch Reviews", "Rank", "Course information", "SignUp to Course",
    "Course Participants"};
    String[] item_desc= {"Previous reviews on this course/lecturer", "Rank the Lecturer", "Syllabus, " +
            "Class, Credits..", "Register as course participant", "View/chat all course participants"};

    Integer[] imageId = {R.drawable.rank_icon,R.drawable.star_icon, R.drawable.info_icon,R.drawable.sign_up_icon,
    R.drawable.review_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_details);

        final Intent intent = getIntent();
        intendMes = intent.getStringArrayExtra("values");
        hashMap_rating = (HashMap<String, ArrayList<rating_lecterer_model>>)intent.getSerializableExtra("RatingMap");
        courseDetailsModel = (CourseDetailsModel)intent.getSerializableExtra("CourseDetails");
        textViewTitle = (TextView) findViewById(R.id.title_text) ;
        textViewTitle.setText(intendMes[2] + ", " + intendMes[3] );
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        myActionBar = (Toolbar) findViewById(R.id.toolbar2);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CustomListAdapter adapter = new CustomListAdapter(this,items,imageId, item_desc);
        cheackIfUserSignedUp(adapter);

        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

        //adapter.SetCahngeInItemList("Hi man", 3);
        //adapter.notifyDataSetChanged();

        final Activity myrefAct = this;
        final CustomListAdapter madapter = adapter;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){ //Watch reviews
                    ArrayList<rating_lecterer_model> rating = hashMap_rating.get(intendMes[3]);
                    Intent intentWatchRev = new Intent(myrefAct,WatchReviews.class);
                    intentWatchRev.putExtra("Rating",rating);
                    intentWatchRev.putExtra("values", intendMes);
                    startActivity(intentWatchRev);
                    //finish();   //  TODO: decide if to finish or if we have the options to pull back
                } else if (position == 1){// Rank
                    if (intendMes[4].equals("All"))
                        Toast.makeText(getApplicationContext(),"You must choose specific semester\n " +
                                "please back to previous screen.", Toast.LENGTH_LONG).show();
                    else{
                        Intent RankIntend = new Intent(myrefAct,Course_Lacturer_rank.class);
                        RankIntend.putExtra("values",intendMes);
                        startActivity(RankIntend);
                    }
                } else if (position == 2) { //Course Information
                    if (courseDetailsModel != null){
                        Intent CourseDetIntend = new Intent(myrefAct,CorseDetails.class);
                        CourseDetIntend.putExtra("values",courseDetailsModel);
                        CourseDetIntend.putExtra("courseName", intendMes[2]);
                        startActivity(CourseDetIntend);
                    } else {
                        Toast.makeText(getApplicationContext(), "Could not find course information", Toast.LENGTH_LONG).show();
                    }
                } else if (position == 3) { //signUp to course
                    if (firebaseUser != null && (!intendMes[4].equals("All"))) {
                        databaseReferenceDelAdd = FirebaseDatabase.getInstance().
                                getReference("Academy/" + intendMes[1] + "/Faculty/" + intendMes[0]
                                + "/Course/" + intendMes[2] + "/" + intendMes[4] + "/Course Participants");

                        if (registeredToCourse) {
                            databaseReferenceDelAdd.child(createNewEmailKey(firebaseUser.getEmail().split("\\."))).removeValue();
                            madapter.SetCahngeInItemList("SignUp to Course", 3);
                            madapter.notifyDataSetChanged();
                            registeredToCourse = false;
                        } else {
                            String tmp = createNewEmailKey(firebaseUser.getEmail().split("\\."));
                            databaseReferenceDelAdd.child(tmp).
                                    setValue(LoginActivity.user_ref.getUserName());
                            madapter.SetCahngeInItemList("Unsubscribe to course", 3);
                            madapter.notifyDataSetChanged();
                            registeredToCourse = true;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "You must choose specific semester,\n" +
                                "please go to previous screen",Toast.LENGTH_LONG).show();
                    }
                } else if (position == 4){ //Course participants

                }
            }
        });
    }

    public String createNewEmailKey(String[] emailSplit)
    {
        String newEmail = "";
        for (int i = 0; i < emailSplit.length; i++ )
                newEmail += emailSplit[i];
        return  newEmail;
    }


    public void cheackIfUserSignedUp(CustomListAdapter adapter){

        final CustomListAdapter mAdapder  = adapter;
        //final String userEmail = firebaseUser.getEmail();
        final String userEmail = createNewEmailKey(firebaseUser.getEmail().split("\\."));

        if (firebaseUser != null && (!intendMes[4].equals("All"))){ //cannot sign up with all selected
            databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference.child("Academy/" + intendMes[1] + "/Faculty/" + intendMes[0]
            + "/Course/" + intendMes[2] + "/" + intendMes[4] + "/Course Participants").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot mDataSnapshot: dataSnapshot.getChildren()){
                                if (mDataSnapshot.getKey().equals(userEmail)){ //User already signed up
                                    mAdapder.SetCahngeInItemList("Unsubscribe to course", 3);
                                    mAdapder.notifyDataSetChanged();
                                    registeredToCourse = true;
                                }
                                else {  //not signedUp
                                    mAdapder.SetCahngeInItemList("SignUp to Course", 3);
                                    mAdapder.notifyDataSetChanged();
                                    registeredToCourse = false;
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });
        }

    }
}
