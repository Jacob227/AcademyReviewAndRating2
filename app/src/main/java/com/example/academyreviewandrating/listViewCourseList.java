package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.academyreviewandrating.Model.CourseDetailsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * List view with all course Data
 * Using ExpandableListAdapter
 */
public class listViewCourseList extends AppCompatActivity {

    private Toolbar myActionBar;
    private TextView textViewTitle;
    private ArrayList<CourseDetailsModel> mCourseListDet;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    /**
     * Init all View components
     * including ExpandableListView
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_course_list);

        Intent mIntend = getIntent();
        mCourseListDet = (ArrayList<CourseDetailsModel>)mIntend.getSerializableExtra("arraylistCourse");

        myActionBar = (Toolbar) findViewById(R.id.toolbar_timetableList);
        myActionBar.setNavigationIcon(getResources().getDrawable(R.drawable.back_arrow_icon));
        myActionBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        expListView = (ExpandableListView) findViewById(R.id.Exlist_view);

        prepareData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

    }

    /**
     * insert to Arraylist all the course information
     */
    public void prepareData(){
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        int i = 0;
        for (CourseDetailsModel myCorse: mCourseListDet) {
            listDataHeader.add(myCorse.getCourse_name());
            List<String> myCourseDet = new ArrayList<String >();
            myCourseDet.add("Time: " + myCorse.getTime());
            myCourseDet.add("Room:  " + myCorse.getRoom());
            myCourseDet.add("Credits:  " + myCorse.getCredits());
            myCourseDet.add("Code Course:  " + myCorse.getCode_course());
            myCourseDet.add("Syllabus:  " + myCorse.getSyllabus());
            listDataChild.put(listDataHeader.get(i), myCourseDet);
            i++;
        }
    }
}
