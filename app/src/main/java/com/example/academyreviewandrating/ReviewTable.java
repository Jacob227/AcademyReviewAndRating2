package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.rating_lecterer_model;

import java.util.ArrayList;

public class ReviewTable extends AppCompatActivity {

    private ArrayList<rating_lecterer_model> ratingDet;
    private String[] intendMes;
    private TextView textViewTitle,textViewFaculty, textViewLecturerName, textViewRevNumber;
    private TextView textViewCourseLevel, textViewLecAtt,textViewAbility, textViewLecInter;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private Toolbar myActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_table);

        final Intent intent = getIntent();
        ratingDet = (ArrayList<rating_lecterer_model>)intent.getSerializableExtra("Rating");
        intendMes = intent.getStringArrayExtra("values");

        textViewTitle = (TextView) findViewById(R.id.title_text_review);
        textViewTitle.setText(intendMes[2] + ", " + intendMes[3]);

        myActionBar = (Toolbar) findViewById(R.id.toolbar_table);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntend = new Intent(getApplicationContext(),WatchReviews.class);
                backIntend.putExtra("Rating",ratingDet);
                backIntend.putExtra("values", intendMes);
                startActivity(backIntend);
                finish();
            }
        });

        tableLayout = (TableLayout) findViewById(R.id.table_layout);
        //tableRow = (TableRow) findViewById(R.id.tableRow1);
        int i = 1;
        for (rating_lecterer_model rating: ratingDet) {
            tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.attribute_row, null);
            ((TextView) tableRow.findViewById(R.id.Number_col1)).setText(String.valueOf(i));

            if (rating.getAnonymous()) { //the user is anonymous
                ((TextView) tableRow.findViewById(R.id.Reviewer_col1)).setText(" - ");   //TODO: check if we can view this reviewer
            } else { //un-anonymous
                ((TextView) tableRow.findViewById(R.id.Reviewer_col1)).setText(rating.get_rank_name());
            }
            ((TextView) tableRow.findViewById(R.id.rank_date_col1)).setText(rating.getDate());
            ((TextView) tableRow.findViewById(R.id.Course_Level_col1)).setText(String.valueOf(rating.getCourse_level()));
            ((TextView) tableRow.findViewById(R.id.Lecturer_Atti_cols1)).setText(String.valueOf(rating.getAttitude_lecturer_student()));
            ((TextView) tableRow.findViewById(R.id.Motivation_to_teach_col1)).setText(String.valueOf(rating.getAbility_to_teach()));
            ((TextView) tableRow.findViewById(R.id.lecturer_interesting_col1)).setText(String.valueOf(rating.getTeacher_interesting()));
            ((TextView) tableRow.findViewById(R.id.few_words_col1)).setText(rating.getFew_words());
            tableLayout.addView(tableRow);
            i++;
        }

    }
}
