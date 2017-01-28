package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_lecterer_model;

import java.util.ArrayList;

/**
 * Create Student timetable of his courses that he signed up
 */
public class ScheduleTable extends AppCompatActivity {

    private TableLayout tableLayout;
    private TableRow tableRow;
    private Toolbar myActionBar;
    private ArrayList<CourseDetailsModel> mCourseListDet;
    private String semesterStr;
    private String[][] timeTable = new String[12][6];

    /**
     * Init all view components
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_table);

        Intent mIntend = getIntent();
        semesterStr = mIntend.getStringExtra("values");
        mCourseListDet = (ArrayList<CourseDetailsModel>)mIntend.getSerializableExtra("arraylistCourse");

        myActionBar = (Toolbar) findViewById(R.id.toolbar_table_sch);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tableLayout = (TableLayout) findViewById(R.id.table_layout_sch);
        //tableRow = (TableRow) findViewById(R.id.tableRow1);
        //int i = 1;
        for (int i = 0; i < 12; i++){
            for (int j = 0; j < 6; j++){
                timeTable[i][j] = "";
            }
        }

        for (CourseDetailsModel myCourses: mCourseListDet){
            String[] daysAndHours;
            if (myCourses.getTime().contains(";")) {
                daysAndHours = myCourses.getTime().split(";");
            } else {
                daysAndHours = new String[1];
                daysAndHours[0] = myCourses.getTime();
            }
            String[] days = new String[daysAndHours.length];
            String[] hours = new String[daysAndHours.length];

            String[] rooms = myCourses.getRoom().split(",");

            for (int i = 0; i < daysAndHours.length; i++){
                String[] dayAndHour = daysAndHours[i].split(",");
                days[i] = dayAndHour[0].replaceAll("\\s+",""); //remove white spaces
                hours[i] = dayAndHour[1].replaceAll("\\s+","");
                String[] hours_saperated = dayAndHour[1].replaceAll("\\s+","").split("-");
                int hourBegin = Integer.parseInt(hours_saperated[0].split(":")[0]);
                int hourEnd = Integer.parseInt(hours_saperated[1].split(":")[0]);
                //need to insert to 2D of string
                for (int k = hourBegin; k < hourEnd + 1; k++){
                    timeTable[k - 8][checkDayIndex(days[i])] = myCourses.getCourse_name() + "\n" + rooms[i];
                }
            }
        }

        for (int j = 8; j < 20; j++){
            tableRow = (TableRow) LayoutInflater.from(this).inflate(R.layout.attribute_row_hours, null);
            ((TextView) tableRow.findViewById(R.id.hours_col1)).setText(String.valueOf(j) + ":00");
            for (int k = 0; k < 6; k++){
                if (!timeTable[j - 8][k].equals("")){
                    checkWhatCol(k,tableRow,timeTable[j - 8][k]);
                    setTheRestOfrow(k, j,tableRow);
                }
            }
            tableLayout.addView(tableRow);
        }

    }

    /**
     * inerts details to timetable
     * @param ignoreIndex
     * @param j
     * @param tableR
     */
    public void setTheRestOfrow(int ignoreIndex,int j,TableRow tableR){
        ((TextView) tableR.findViewById(R.id.hours_col1)).setText(String.valueOf(j) + ":00\n");
        for (int i = 0; i < 6; i++){
            if (i != ignoreIndex){
                checkWhatCol(i,tableR,"\n");
            }
        }
    }

    /**
     * check which column to insert the course
     * @param index
     * @param tableR
     * @param value
     */
    public void checkWhatCol(int index, TableRow tableR,String value){
        switch (index){
            case (0): ((TextView) tableRow.findViewById(R.id.Sun_col2)).setText(value);
                        break;
            case (1): ((TextView) tableRow.findViewById(R.id.Mon_col3)).setText(value);
                        break;
            case (2): ((TextView) tableRow.findViewById(R.id.Tus_col4)).setText(value);
                        break;
            case (3): ((TextView) tableRow.findViewById(R.id.Wed_col5)).setText(value);
                        break;
            case (4): ((TextView) tableRow.findViewById(R.id.Thr_col6)).setText(value);
                        break;
            case (5): ((TextView) tableRow.findViewById(R.id.Fri_col7)).setText(value);
                        break;
        }
    }

    /**
     * check which day the user register to course
     * @param day
     * @return
     */
    public int checkDayIndex(String day)
    {
        switch (day){
            case "Sunday":
                return 0;
            case "Monday":
                return 1;
            case "Tuesday":
                return 2;
            case "Wednesday":
                return 3;
            case "Thursday":
                return 4;
            case "Friday":
                return 5;
        }
        return 0;
    }

}
