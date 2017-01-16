package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.rating_academy_model;
import com.example.academyreviewandrating.Model.rating_lecterer_model;

import java.util.ArrayList;

public class ReviewTableFaculty extends AppCompatActivity {

    private ArrayList<rating_academy_model> ratingDet;
    private String[] intendMes;
    private TextView textViewTitle;
    private TableLayout tableLayout;
    private TableRow tableRow;
    private Toolbar myActionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_table_faculty);

        Intent intent = getIntent();
        ratingDet = (ArrayList<rating_academy_model>)intent.getSerializableExtra("Rating");
        intendMes = intent.getStringArrayExtra("values");

        textViewTitle = (TextView) findViewById(R.id.title_text_review_fac);
        textViewTitle.setText(intendMes[0] + ", " + intendMes[1]);

        myActionBar = (Toolbar) findViewById(R.id.toolbar_table_fac);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntend = new Intent(getApplicationContext(),Faculty_view.class);
                backIntend.putExtra("Rating",ratingDet);
                backIntend.putExtra("values", intendMes);
                startActivity(backIntend);
                finish();
            }
        });
        tableLayout = (TableLayout) findViewById(R.id.table_layout_fac);
        //tableRow = (TableRow) findViewById(R.id.tableRow1);
        int i = 1;
        for (rating_academy_model rating: ratingDet) {
            tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.attribute_row_faculty, null);
            ((TextView) tableRow.findViewById(R.id.Number_col_fac1)).setText(String.valueOf(i));

            if (rating.getAnonymous()) { //the user is anonymous
                ((TextView) tableRow.findViewById(R.id.Reviewer_col_fac1)).setText(" - ");   //TODO: check if we can view this reviewer
            } else { //un-anonymous
                ((TextView) tableRow.findViewById(R.id.Reviewer_col_fac1)).setText(rating.getRank_name());
            }
            ((TextView) tableRow.findViewById(R.id.rank_date_col_fac1)).setText(rating.getDate());
            ((TextView) tableRow.findViewById(R.id.AcademicDiff_col1)).setText(String.valueOf(rating.getAcademy_difficulty()));
            ((TextView) tableRow.findViewById(R.id.StudChar_col1)).setText(String.valueOf(rating.getStudents_char()));
            ((TextView) tableRow.findViewById(R.id.SocialLife_col1)).setText(String.valueOf(rating.getSocial_life()));
            ((TextView) tableRow.findViewById(R.id.FacultySecr_col1)).setText(String.valueOf(rating.getFaculty_secretary()));
            ((TextView) tableRow.findViewById(R.id.StudUnion_col1)).setText(String.valueOf(rating.getStudent_union()));
            ((TextView) tableRow.findViewById(R.id.few_words_col_fac1)).setText(rating.getFew_words());
            tableLayout.addView(tableRow);
            i++;
        }

    }
}
