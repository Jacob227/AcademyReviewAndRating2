package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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

public class Course_Lacturer_rank extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private TextView faculty_text, academy_text, course_text, lecturer_text;
    private RatingBar course_level_rb, lecterer_atitude_rb, lecterer_intr_rb, lecterer_ability_rb;
    private EditText fewWords_et;
    private DatabaseReference mDB_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__lacturer_rank);

        Intent intent = getIntent();
        String[] message = intent.getStringArrayExtra("values");

        //mDB_ref = FirebaseDatabase.getInstance().getReference("Academy").;
        faculty_text = (TextView) findViewById(R.id.faculty_rank_text);
        academy_text  = (TextView) findViewById(R.id.academy_rank_text);
        course_text = (TextView) findViewById(R.id.course_rank_text);
        lecturer_text = (TextView) findViewById(R.id.lecturer_rank_text);

        course_level_rb = (RatingBar) findViewById(R.id.RatingBar_course_level) ;
        lecterer_atitude_rb = (RatingBar) findViewById(R.id.RatingBar_lecturer_attitude) ;
        lecterer_intr_rb = (RatingBar) findViewById(R.id.RatingBar_lecturer_intresting) ;
        lecterer_ability_rb = (RatingBar) findViewById(R.id.RatingBar_ability_to_study) ;

        fewWords_et = (EditText) findViewById(R.id.editText_few_words) ;

        faculty_text.setText(message[0]);
        academy_text.setText(message[1]);
        course_text.setText(message[2]);
        lecturer_text.setText(message[3]);
    }

    public void sendFeedbackOnClick(View view) {
        view.startAnimation(buttonClick);
        //collect all the data and insert it to DB
        final rating_lecterer_model rl_model = new rating_lecterer_model(Math.round(lecterer_ability_rb.getNumStars()),
                Math.round(lecterer_atitude_rb.getNumStars()), Math.round(course_level_rb.getNumStars()),
                Math.round(lecterer_intr_rb.getNumStars()), fewWords_et.getText().toString(),
                new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        mDB_ref = FirebaseDatabase.getInstance().getReference("Academy").child(academy_text.getText().toString()).child("Faculty").child(faculty_text.getText().toString()).
                child("Course").child(course_text.getText().toString()).child("Lecturer").child(lecturer_text.getText().toString()).
                child("Rating");
//
        mDB_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(LoginActivity.user_ref.getUserName())){
                    Toast.makeText(Course_Lacturer_rank.this,"You already rank this lecturer",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDB_ref.child(LoginActivity.user_ref.getUserName()).setValue(rl_model);
                    Toast.makeText(Course_Lacturer_rank.this,"Thank you for rating",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}
