package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Rank class
 * Ranking the Course/Lecturer view
 */
public class Course_Lacturer_rank extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private TextView faculty_text, academy_text, course_text, lecturer_text, semester_text;
    private RatingBar course_level_rb, lecterer_atitude_rb, lecterer_intr_rb, lecterer_ability_rb;
    private EditText fewWords_et;
    private DatabaseReference mDB_ref;
    private String[] message;
    private String QuerySurvey = null;
    private Toolbar myActionBar;
    private CheckBox cb_anonymous;

    /**
     * Init all view component
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__lacturer_rank);

        Intent intent = getIntent();
        message = intent.getStringArrayExtra("values");
        QuerySurvey = intent.getStringExtra("QuerySurvey");

        myActionBar = (Toolbar) findViewById(R.id.toolbar_Rank);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cb_anonymous = (CheckBox) findViewById(R.id.checkBox_anonymous) ;
        faculty_text = (TextView) findViewById(R.id.faculty_rank_text);
        academy_text  = (TextView) findViewById(R.id.academy_rank_text);
        course_text = (TextView) findViewById(R.id.course_rank_text);
        lecturer_text = (TextView) findViewById(R.id.lecturer_rank_text);
        semester_text = (TextView) findViewById(R.id.semester_rank_text) ;

        course_level_rb = (RatingBar) findViewById(R.id.RatingBar_course_level) ;
        lecterer_atitude_rb = (RatingBar) findViewById(R.id.RatingBar_lecturer_attitude) ;
        lecterer_intr_rb = (RatingBar) findViewById(R.id.RatingBar_lecturer_intresting) ;
        lecterer_ability_rb = (RatingBar) findViewById(R.id.RatingBar_ability_to_study) ;

        fewWords_et = (EditText) findViewById(R.id.editText_few_words) ;
            faculty_text.setText(message[0]);
            academy_text.setText(message[1]);
            course_text.setText(message[2]);
            lecturer_text.setText(message[3]);
            semester_text.setText(message[4]);
    }

    /**
     * Check all the feedback that user chose
     * Insert to DB all the user rating.
     * @param view
     */
    public void sendFeedbackOnClick(View view) {
        view.startAnimation(buttonClick);
        //collect all the data and insert it to DB
        //TODO: check if all starts are chosen!
        Boolean cb_an = true;
        if (!cb_anonymous.isChecked()){
            cb_an = false;
        }
        final rating_lecterer_model rl_model = new rating_lecterer_model(Math.round(lecterer_ability_rb.getRating()),
                Math.round(lecterer_atitude_rb.getRating()), Math.round(course_level_rb.getRating()),
                Math.round(lecterer_intr_rb.getRating()), fewWords_et.getText().toString(),
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()),cb_an);
        if (rl_model.getAnonymous())
            rl_model.set_rank_name("-");
        else
            rl_model.set_rank_name(LoginActivity.user_ref.getUserName());

        if (QuerySurvey != null){
            mDB_ref = FirebaseDatabase.getInstance().getReference(QuerySurvey);
        } else { //not survey
            mDB_ref = FirebaseDatabase.getInstance().getReference("Academy").child(academy_text.getText().toString()).child("Faculty").child(faculty_text.getText().toString()).
                    child("Course").child(course_text.getText().toString()).child(semester_text.getText().toString()).child("Lecturer").child(lecturer_text.getText().toString()).
                    child("Rating");
        }
        mDB_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(LoginActivity.user_ref.getUserName())){
                    Toast.makeText(Course_Lacturer_rank.this,"You already rank this lecturer",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDB_ref.child(LoginActivity.user_ref.getUserName()).setValue(rl_model);
                    Toast.makeText(Course_Lacturer_rank.this,"Thank you for rating",Toast.LENGTH_SHORT).show();
                    if (!ListViewCourseDetails.hasRanked) {
                        ListViewCourseDetails.hasRanked = true;
                        ListViewCourseDetails.addRaiting = rl_model;
                    }
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
