package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.CourseDetailsModel;

public class CorseDetails extends AppCompatActivity {

    private CourseDetailsModel courseDetailsModel;
    private Toolbar mToolBar;
    private TextView textViewTitle, textViewCodeCourse, textViewCredits;
    private TextView textViewRoom, textViewSyllabus, textViewTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corse_details);

        Intent intent = getIntent();
        courseDetailsModel = (CourseDetailsModel) intent.getSerializableExtra("values");

        textViewTitle = (TextView) findViewById(R.id.title_text_course_details);
        textViewTitle.setText(intent.getStringExtra("courseName"));

        textViewCodeCourse = (TextView) findViewById(R.id.Code_course_text2);
        textViewCodeCourse.setText(String.valueOf(Math.round(courseDetailsModel.getCode_course())));
        textViewCredits = (TextView) findViewById(R.id.Credits_text2);
        textViewCredits.setText(String.valueOf(Math.round(courseDetailsModel.getCredits())));
        textViewRoom = (TextView) findViewById(R.id.Room_text2);
        textViewRoom.setText(courseDetailsModel.getRoom());
        textViewSyllabus = (TextView) findViewById(R.id.Syllabus_text2);
        textViewSyllabus.setText(courseDetailsModel.getSyllabus());
        textViewTime = (TextView) findViewById(R.id.Time_text2);
        textViewTime.setText(courseDetailsModel.getTime());

        mToolBar = (Toolbar) findViewById(R.id.toolbar_course_details);
        mToolBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }
}
