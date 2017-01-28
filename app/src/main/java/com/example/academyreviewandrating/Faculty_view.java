package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_academy_model;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * View the students rating average
 */
public class Faculty_view extends AppCompatActivity {

    private String[] intendMes;
    private Toolbar myActionBar;
    private DatabaseReference mDatebase;
    private ArrayList<rating_academy_model> academy_rating;
    private TextView textViewTitle,textViewFaculty, textViewFAcademy, textViewAcademicDiff, textViewStudChar;
    private TextView textViewSocialLife, textViewFacultySecr,textViewStudUnion;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);

    /**
     * Init all view components
     * get all students rating information from DB
     * Calculate avg
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view);

        Intent intent = getIntent();
        intendMes = intent.getStringArrayExtra("values");   //faculty 0, academy 1
        academy_rating = new ArrayList<rating_academy_model>();

        myActionBar = (Toolbar) findViewById(R.id.toolbar_review_faculty);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        textViewFAcademy = (TextView) findViewById(R.id.academy_text2);
        textViewFaculty = (TextView) findViewById(R.id.Faculty_text2);
        textViewTitle = (TextView) findViewById(R.id.title_text_review_faculty);

        textViewAcademicDiff = (TextView) findViewById(R.id.Academic_Difficulty_text2);
        textViewStudChar = (TextView) findViewById(R.id.Students_Char_text2);
        textViewSocialLife = (TextView) findViewById(R.id.Social_life_text2);
        textViewFacultySecr = (TextView) findViewById(R.id.Faculty_secr_text2);
        textViewStudUnion = (TextView) findViewById(R.id.Students_Union_text2);

        textViewFAcademy.setText(intendMes[1]);
        textViewFaculty.setText(intendMes[0]);
        textViewTitle.setText("Watch Rating Reviews");

        mDatebase = FirebaseDatabase.getInstance().getReference("Academy/" + intendMes[1] + "/Faculty/" +
        intendMes[0] + "/Rating_faculty");

        mDatebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChildren()){
                    Toast.makeText(Faculty_view.this, "No one rank this Faculy", Toast.LENGTH_LONG).show();
                    finish();
                }
                else {
                    for (DataSnapshot rating: dataSnapshot.getChildren()){
                        rating_academy_model ratingAcademyModel = rating.getValue(rating_academy_model.class);
                        ratingAcademyModel.setRank_name(rating.getKey());
                        academy_rating.add(ratingAcademyModel);
                    }
                    //calc avg
                   int avg_academic_diff = 0, avg_stud_char = 0,avg_social_life = 0,
                           avg_faculty_secr = 0, avg_stud_union = 0;
                    for (rating_academy_model ratingAcademyModel: academy_rating){
                        avg_academic_diff += ratingAcademyModel.getAcademy_difficulty();
                        avg_stud_char += ratingAcademyModel.getStudents_char();
                        avg_social_life += ratingAcademyModel.getSocial_life();
                        avg_faculty_secr += ratingAcademyModel.getFaculty_secretary();
                        avg_stud_union += ratingAcademyModel.getStudent_union();
                    }
                    int size = academy_rating.size();
                    textViewAcademicDiff.setText(String.valueOf(avg_academic_diff/size));
                    textViewStudChar.setText(String.valueOf(avg_stud_char/size));
                    textViewSocialLife.setText(String.valueOf(avg_social_life/size));
                    textViewFacultySecr.setText(String.valueOf(avg_faculty_secr/size));
                    textViewStudUnion.setText(String.valueOf(avg_stud_union/size));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * On click Watch Table button
     * pass data to next intent to display all rating on table
     * @param view
     */
    public void OnClickWatchTableFaculty(View view) {
        view.startAnimation(buttonClick);
        Intent intentTable = new Intent(this, ReviewTableFaculty.class);
        intentTable.putExtra("Rating",academy_rating);
        intentTable.putExtra("values", intendMes);
        startActivity(intentTable);
        finish();

    }
}
