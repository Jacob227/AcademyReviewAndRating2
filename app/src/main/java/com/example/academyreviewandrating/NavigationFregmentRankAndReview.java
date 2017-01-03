package com.example.academyreviewandrating;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by יעקב on 04/12/2016.
 */

public class NavigationFregmentRankAndReview extends Fragment {

    private DatabaseReference mDatebase;
    private Button faculty_rank_button, faculty_view_button, teacher_rank_button, teacher_view_button;
    private Spinner academy_spinner,faculty_spinner,course_spinner,teacher_spinner, semester_spinner;
    private String[] string_spinner_academy, string_spinner_faculty, strings_spinner_course, strings_spinner_teacher ;
    private ArrayAdapter<String> adapter;
    private String academy_selected, faculty_selected, course_selected, teacher_selected, semester_selected;
    private List<String> List_spinner;
    private Activity ref_activity;
    private Handler handler;
    private Boolean faculty_chosen;
    private Boolean course_chosen;
    private boolean flag_user_ref = false;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    View myView;

    public enum spinnerEnum {
        ACADEMY, FACULTY, COURSE, TEACHER, SEMESTER
    }

    public NavigationFregmentRankAndReview() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.choose_rank_or_review,container,false);

        Log.d("onCreateView ","NavigationRankRev");
        return myView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        handler = new Handler();
        setAllListener();
        faculty_chosen = false;
        course_chosen = false;

        fillSpinnerData(R.id.spinner_academy,"Academy", spinnerEnum.ACADEMY);
        Log.d("In onViewCreated", "after fillSpinnerData Academy");
        //academy_selected = LoginActivity.user_ref.getInstitution(); //user defualt academic
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (LoginActivity.user_ref != null) {
                    academy_selected = LoginActivity.user_ref.getInstitution();
                    fillSpinnerData(R.id.spinner_faculty,
                            "Academy/" + academy_selected + "/Faculty", spinnerEnum.FACULTY);
                    //faculty_selected = LoginActivity.user_ref.getFaculty();
                                        /*
                    while (!faculty_chosen);
                    Log.d("In run","Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course");
                    fillSpinnerData(R.id.spinner_choose_course,
                            "Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course" , spinnerEnum.COURSE);

                    while (course_selected == "");
                    Log.d("In run after Course", "Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course/" + course_selected
                            + "/Lecturer");
                    fillSpinnerData(R.id.spinner_choose_teacher,
                            "Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course/" + course_selected
                                    + "/Lecturer", spinnerEnum.TEACHER);*/
                }
                else {
                    Toast.makeText(getActivity(),"Timeout has accured",Toast.LENGTH_LONG).show();
                }
            }
        }, 3500);
        //faculty_selected = LoginActivity.user_ref.getFaculty(); //user default faculty
    }

    public void setAllListener(){
        final Activity my_activity = getActivity();
        faculty_rank_button = (Button) getView().findViewById(R.id.button_Rank_Faculty);
        faculty_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (faculty_selected == ""){
                    Toast.makeText(getActivity(),"Please insert Faculty",Toast.LENGTH_LONG).show();
                }
                else if(academy_selected == "")
                    Toast.makeText(getActivity(),"Please insert academy",Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(my_activity,Faculty_rank.class);
                    String[] data = {faculty_selected,academy_selected};
                    intent.putExtra("values",data);
                    startActivity(intent);
                }
            }
        });

        faculty_view_button = (Button) getView().findViewById(R.id.button_Fac_View);
        faculty_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (faculty_selected == ""){
                    Toast.makeText(getActivity(),"Please insert Faculty",Toast.LENGTH_LONG).show();
                }
                else if(academy_selected == "")
                    Toast.makeText(getActivity(),"Please insert academy",Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(my_activity,Faculty_view.class);
                    String[] data = {faculty_selected,academy_selected};
                    intent.putExtra("values",data);
                    startActivity(intent);
                }
            }
        });

        teacher_rank_button  = (Button) getView().findViewById(R.id.button_rank_teacher);
        teacher_rank_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (faculty_selected == ""){
                    Toast.makeText(getActivity(),"Please insert Faculty",Toast.LENGTH_LONG).show();
                }
                else if(academy_selected == "")
                    Toast.makeText(getActivity(),"Please insert academy",Toast.LENGTH_LONG).show();
                else if(course_selected == "")
                    Toast.makeText(getActivity(),"Please insert course",Toast.LENGTH_LONG).show();
                else if (teacher_selected == "")
                    Toast.makeText(getActivity(),"Please insert lecturer",Toast.LENGTH_LONG).show();
                else{
                    Intent intent = new Intent(my_activity,Course_Lacturer_rank.class);
                    String[] data = {faculty_selected,academy_selected,course_selected,teacher_selected,semester_selected};
                    intent.putExtra("values",data);
                    startActivity(intent);
                }

            }
        });

        teacher_view_button = (Button) getView().findViewById(R.id.button_view_teacher);
        teacher_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (faculty_selected == ""){
                    Toast.makeText(getActivity(),"Please insert Faculty",Toast.LENGTH_LONG).show();
                }
                else if(academy_selected == "")
                    Toast.makeText(getActivity(),"Please insert academy",Toast.LENGTH_LONG).show();
                else if(course_selected == "")
                    Toast.makeText(getActivity(),"Please insert course",Toast.LENGTH_LONG).show();
                else if (teacher_selected == "")
                    Toast.makeText(getActivity(),"Please insert lecturer",Toast.LENGTH_LONG).show();
                else{
                    //Need to tranfer data
                    Intent intent = new Intent(my_activity, ListViewCourseDetails.class);
                    startActivity(intent);
                }

            }
        });

        academy_selected = faculty_selected = course_selected = teacher_selected = "";
        academy_spinner = (Spinner) getView().findViewById(R.id.spinner_academy);

        academy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                academy_selected = parent.getSelectedItem().toString();
                fillSpinnerData(R.id.spinner_faculty,
                        "Academy/" + academy_selected + "/Faculty", spinnerEnum.FACULTY);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        faculty_spinner = (Spinner) getView().findViewById(R.id.spinner_faculty);
        faculty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                faculty_selected = parent.getSelectedItem().toString();
                fillSpinnerData(R.id.spinner_choose_course,
                        "Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course" , spinnerEnum.COURSE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        course_spinner = (Spinner) getView().findViewById(R.id.spinner_choose_course);
        course_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                course_selected = parent.getSelectedItem().toString();
                fillSpinnerData(R.id.spinner_choose_semester,"Academy/" + academy_selected + "/Faculty/"
                        + faculty_selected + "/Course/" + course_selected,spinnerEnum.SEMESTER);


                       // fillSpinnerData(R.id.spinner_choose_semester,"Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course/" + course_selected
                       //         + "/Lecturer", spinnerEnum.TEACHER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        semester_spinner = (Spinner) getView().findViewById(R.id.spinner_choose_semester);
        semester_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester_selected = parent.getSelectedItem().toString();

                fillSpinnerData(R.id.spinner_choose_teacher,"Academy/" + academy_selected + "/Faculty/" + faculty_selected + "/Course/" + course_selected
                        + "/"  + semester_selected + "/Lecturer", spinnerEnum.TEACHER);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        teacher_spinner = (Spinner) getView().findViewById(R.id.spinner_choose_teacher);
        teacher_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                teacher_selected = parent.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    public void fillSpinnerData( int id, String child_name, spinnerEnum defualt_spinner ){
        final spinnerEnum index_spinn = defualt_spinner;
        final Spinner my_spinner = (Spinner) getView().findViewById(id);
        mDatebase = FirebaseDatabase.getInstance().getReference(child_name);
        ref_activity = this.getActivity();

        mDatebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List_spinner = new ArrayList<String>();
                Log.d("In onDataChange","top");
                String def_value_of_spinner = "";
                if (index_spinn == spinnerEnum.ACADEMY && LoginActivity.user_ref != null) {
                    def_value_of_spinner = LoginActivity.user_ref.getInstitution();
                    Log.d("spinnerEnum.ACADEMY","LoginActivity.user_ref != null");
                }

                for (DataSnapshot child: dataSnapshot.getChildren()){
                    if (!def_value_of_spinner.equals(child.getKey()))
                        List_spinner.add(child.getKey());
                }
                //test
                List_spinner.add(0, def_value_of_spinner);
                String[] spinner_str = new String[List_spinner.size()];
                spinner_str = List_spinner.toArray(spinner_str);

                adapter = new ArrayAdapter<String>(ref_activity,
                        android.R.layout.simple_spinner_item, spinner_str);
                my_spinner.setAdapter(adapter);

                if (index_spinn == spinnerEnum.ACADEMY)
                    academy_selected = List_spinner.get(0);
                else if (index_spinn == spinnerEnum.FACULTY) {
                    faculty_selected = List_spinner.get(0);
                    synchronized (faculty_chosen) {
                        faculty_chosen = true;
                    }
                }
                else if (index_spinn == spinnerEnum.COURSE) {
                    course_selected = List_spinner.get(0);
                    synchronized (course_chosen) {
                        course_chosen = true;
                    }
                }
                else if (index_spinn == spinnerEnum.TEACHER)
                    teacher_selected = List_spinner.get(0);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void spinnerAcademyOnClick (View view){
        String itemSel = academy_spinner.getSelectedItem().toString();
        Log.d("hi", itemSel);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d("In RankAndReview","onDestroy");
        handler.removeCallbacksAndMessages(null);

    }
}
