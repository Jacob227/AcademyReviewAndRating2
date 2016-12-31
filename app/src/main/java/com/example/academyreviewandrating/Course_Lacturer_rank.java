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

public class Course_Lacturer_rank extends AppCompatActivity {

    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private TextView faculty_text, academy_text, course_text, lecturer_text;
    private RatingBar course_level_rb, lecterer_atitude_rb, lecterer_intr_rb, lecterer_ability_rb;
    private EditText fewWords_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course__lacturer_rank);

        Intent intent = getIntent();
        String[] message = intent.getStringArrayExtra("values");

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



    }

}
