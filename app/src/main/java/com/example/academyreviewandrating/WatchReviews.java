package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.rating_lecterer_model;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class WatchReviews extends AppCompatActivity {

    private ArrayList<rating_lecterer_model> ratingDet;
    private String[] intendMes;
    private Toolbar myActionBar;
    private TextView textViewTitle,textViewFaculty, textViewLecturerName, textViewRevNumber;
    private TextView textViewCourseLevel, textViewLecAtt,textViewAbility, textViewLecInter;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_reviews);

        Intent intent = getIntent();
        ratingDet = (ArrayList<rating_lecterer_model>)intent.getSerializableExtra("Rating");
        intendMes = intent.getStringArrayExtra("values");

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

    public void OnClickWatchTable(View view){
        view.startAnimation(buttonClick);
        Intent intentTableRev = new Intent(this, ReviewTable.class);
        intentTableRev.putExtra("Rating",ratingDet);
        intentTableRev.putExtra("values", intendMes);
        startActivity(intentTableRev);
        finish();
    }
}
