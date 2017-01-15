package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.rating_academy_model;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Faculty_rank extends AppCompatActivity {

    private String[] mDataIntend;
    private RatingBar rb_academy_diff, rb_students_char, rb_social_life, rb_faculty_secretary, rb_stud_union;
    private EditText editText_few_words;
    private TextView tv_academy, tv_faculty;
    private CheckBox cb_anonymous;
    private DatabaseReference mDB_ref;
    private Toolbar myActionBar;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private Button button_send_feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_rank);

        Intent intent = getIntent();
        mDataIntend = intent.getStringArrayExtra("values");

        myActionBar = (Toolbar) findViewById(R.id.toolbar_Rank_faculty);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rb_academy_diff = (RatingBar) findViewById(R.id.RatingBar_Academic_Diff);
        rb_students_char = (RatingBar) findViewById(R.id.RatingBar_students_character);
        rb_social_life = (RatingBar) findViewById(R.id.RatingBar_Social_life);
        rb_faculty_secretary = (RatingBar) findViewById(R.id.RatingBar_Faculty_secretary);
        rb_stud_union = (RatingBar) findViewById(R.id.RatingBar_students_union);

        editText_few_words = (EditText) findViewById(R.id.editText_few_words_faculty);

        cb_anonymous = (CheckBox) findViewById(R.id.checkBox_anonymous_faculty);
        button_send_feedback = (Button) findViewById(R.id.button_rank_in_rating_faculty);

        tv_academy = (TextView) findViewById(R.id.academy_rank_text);
        tv_faculty = (TextView) findViewById(R.id.faculty_rank_text);

        tv_faculty.setText(mDataIntend[0]);
        tv_academy.setText(mDataIntend[1]);


    }

    public void sendFeedbackOnClick(View view) {
        view.startAnimation(buttonClick);
        //collect all the data and insert it to DB
        //TODO: check if all starts are chosen!
        Boolean cb_an = true;
        if (!cb_anonymous.isChecked()){
            cb_an = false;
        }
        final rating_academy_model rl_model = new rating_academy_model(Math.round(rb_academy_diff.getRating()),
                Math.round(rb_students_char.getRating()), Math.round(rb_social_life.getRating()),
                Math.round(rb_faculty_secretary.getRating()),Math.round(rb_stud_union.getRating())
                ,editText_few_words.getText().toString(),cb_an, new SimpleDateFormat("dd-MM-yyyy").format(new Date()));

        mDB_ref = FirebaseDatabase.getInstance().getReference("Academy/" + mDataIntend[1] + "/Faculty/" +
                     mDataIntend[0] + "/Rating_faculty");
//
        mDB_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(LoginActivity.user_ref.getUserName())){
                    Toast.makeText(Faculty_rank.this,"You already rank this faculty",Toast.LENGTH_SHORT).show();
                }
                else {
                    mDB_ref.child(LoginActivity.user_ref.getUserName()).setValue(rl_model);
                    Toast.makeText(Faculty_rank.this,"Thank you for rating",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
