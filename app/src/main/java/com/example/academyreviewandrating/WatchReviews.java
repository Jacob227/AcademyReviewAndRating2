package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * View the lecturer / course students rating average
 */
public class WatchReviews extends AppCompatActivity {

    private ArrayList<rating_lecterer_model> ratingDet;
    private String classSurvey = null;
    private String[] intendMes;
    private Toolbar myActionBar;
    private List<String> List_spinner;
    private DatabaseReference mDatebase;
    private HashMap<String, ArrayList<rating_lecterer_model>> map_lecrurer;
    private CourseDetailsModel courseDetailsModel;
    private TextView textViewTitle,textViewFaculty, textViewLecturerName, textViewRevNumber;
    private TextView textViewCourseLevel, textViewLecAtt,textViewAbility, textViewLecInter;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);

    /**
     *  Init all view components
     * get all students rating information from DB
     * Calculate avg
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_reviews);

        Intent intent = getIntent();
        ratingDet = (ArrayList<rating_lecterer_model>)intent.getSerializableExtra("Rating");
        intendMes = intent.getStringArrayExtra("values");
        classSurvey = intent.getStringExtra("classSurvey");

        map_lecrurer = new HashMap<String, ArrayList<rating_lecterer_model>>();
        if (classSurvey == null) {

            mDatebase = FirebaseDatabase.getInstance().getReference("Academy/" +
                    intendMes[1] + "/Faculty/" + intendMes[0] + "/Course/"
                    + intendMes[2]);

            mDatebase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List_spinner = new ArrayList<String>();
                    Log.d("In onItemSelected", "All semester selected");
                    boolean first = false;
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (!first) { //take the first Course details onlt once
                            DataSnapshot courseDet = child.child("Course Details");
                            courseDetailsModel = courseDet.getValue(CourseDetailsModel.class);
                            first = true;
                        }

                        DataSnapshot lecData = child.child("Lecturer").child(intendMes[3]);
                        for (DataSnapshot childLec : lecData.getChildren()) { //lecturer
                            DataSnapshot rating_data = childLec.child("Rating");
                            for (DataSnapshot childRating : rating_data.getChildren()) { //rating
                                if (map_lecrurer.get(childLec.getKey()) == null) {
                                    map_lecrurer.put(childLec.getKey(), new ArrayList<rating_lecterer_model>());
                                }
                                rating_lecterer_model user_rating_data = childRating.getValue(rating_lecterer_model.class);
                                user_rating_data.set_rank_name(childRating.getKey());
                                map_lecrurer.get(childLec.getKey()).add(user_rating_data);
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        myActionBar = (Toolbar) findViewById(R.id.toolbar_review);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewTitle = (TextView) findViewById(R.id.title_text_review);
        textViewTitle.setText(intendMes[2]);
        textViewFaculty = (TextView) findViewById(R.id.Faculty_text2);
        textViewFaculty.setText(intendMes[0]);
        textViewLecturerName = (TextView) findViewById(R.id.lecurer_Name_text2);
        textViewLecturerName.setText(intendMes[3]);

        int ratingDet_size = ratingDet.size();
        textViewRevNumber = (TextView) findViewById(R.id.Reviewers_Number_text2);
        textViewRevNumber.setText(String.valueOf(ratingDet_size));

        int avg = 0;
        for( rating_lecterer_model rating: ratingDet) {
            avg += rating.getCourse_level();
        }

        textViewCourseLevel = (TextView) findViewById(R.id.Course_Level_text2);
        textViewCourseLevel.setText(String.valueOf(avg/ratingDet_size));

        avg = 0;
        for( rating_lecterer_model rating: ratingDet) {
            avg += rating.getAttitude_lecturer_student();
        }
        textViewLecAtt = (TextView) findViewById(R.id.Lecturer_Atitude_text2);
        textViewLecAtt.setText(String.valueOf(avg/ratingDet_size));

        avg = 0;
        for( rating_lecterer_model rating: ratingDet) {
            avg += rating.getAbility_to_teach();
        }
        textViewAbility = (TextView) findViewById(R.id.Ability_to_Teach_text2);
        textViewAbility.setText(String.valueOf(avg/ratingDet_size));

        avg = 0;
        for( rating_lecterer_model rating: ratingDet) {
            avg += rating.getAbility_to_teach();
        }
        textViewLecInter = (TextView) findViewById(R.id.lecturer_interesting_text2);
        textViewLecInter.setText(String.valueOf(avg/ratingDet_size));

    }

    /**
     * On click Watch Table button
     * pass data to next intent to display all rating on table
     * @param view
     */
    public void OnClickWatchTable(View view){
        view.startAnimation(buttonClick);
        Intent intentTableRev = new Intent(this, ReviewTable.class);
        if (map_lecrurer.isEmpty())
            intentTableRev.putExtra("Rating",ratingDet);
        else {
            Log.d("In OnClickWatchTable","After");
            ArrayList<rating_lecterer_model> rating = map_lecrurer.get(intendMes[3]);
            intentTableRev.putExtra("Rating", rating);
        }
        intentTableRev.putExtra("values", intendMes);
        startActivity(intentTableRev);
        finish();
    }
}
