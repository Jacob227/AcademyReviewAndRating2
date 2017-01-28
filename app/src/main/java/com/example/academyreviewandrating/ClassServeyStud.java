package com.example.academyreviewandrating;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * The user can start to rank the lecturer
 * that open a new survey (Lecturer must open survey before)
 */
public class ClassServeyStud extends Fragment {

    private View myView;
    private Button bt_stop, bt_start;
    private Spinner sp_semester,sp_course, sp_lec_name;
    private DatabaseReference firebaseDatabase;
    private Activity ref_activity;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private String query = "Academy/" +
            LoginActivity.user_ref.getInstitution() + "/Faculty/"
            + LoginActivity.user_ref.getFaculty() + "/Lecturer_faculty/";
           // LoginActivity.user_ref.getUserName() + "/Courses/";
    private Boolean bt_startPressed = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_class_servey_stud,container,false);
        Log.d("onCreateView ","NavigationSurvey");
        return myView;
    }

    /**
     * Init all View components
     * Get academy, lecturer, semester data from DB
     * init start ranking button listener
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt_start = (Button) getView().findViewById(R.id.choose_start_but);

        ref_activity = getActivity();
        sp_semester = (Spinner) getView().findViewById(R.id.spinner_sem);
        sp_course = (Spinner) getView().findViewById(R.id.spinner_course);
        sp_lec_name = (Spinner) getView().findViewById(R.id.spinner_lec_name);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference(query);

        firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> List_inst = new ArrayList<String>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    List_inst.add(child.getKey());
                }
                String[] semester_str = new String[List_inst.size()];
                semester_str = List_inst.toArray(semester_str);
                ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                sp_lec_name.setAdapter(adapterSem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sp_lec_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final AdapterView<?> fadapterView = adapterView;
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(query +
                        fadapterView.getSelectedItem().toString() + "/Courses");
                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<String> List_inst = new ArrayList<String>();
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            List_inst.add(child.getKey());
                        }
                        String[] semester_str = new String[List_inst.size()];
                        semester_str = List_inst.toArray(semester_str);
                        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                        sp_course.setAdapter(adapterSem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sp_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final AdapterView<?> fadapterView = adapterView;
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(query +
                         sp_lec_name.getSelectedItem().toString()+ "/Courses/" +
                        fadapterView.getSelectedItem().toString() + "/Semesters" );
                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<String> List_inst = new ArrayList<String>();
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            List_inst.add(child.getKey());
                        }
                        String[] semester_str = new String[List_inst.size()];
                        semester_str = List_inst.toArray(semester_str);
                        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                        sp_semester.setAdapter(adapterSem);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_startPressed = true;
                view.setAnimation(buttonClick);
                final String course = sp_course.getSelectedItem().toString();
                final String semester = sp_semester.getSelectedItem().toString();
                final String Lec_name = sp_lec_name.getSelectedItem().toString();
                final String mQuery = query + Lec_name + "/Courses/" + course + "/Semesters/" + semester; //"/Survey Act"
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(mQuery + "/Survey Act");
                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().toString().equals("Survey Act")) {
                            if ((Boolean) dataSnapshot.getValue() == false) {
                                Toast.makeText(ref_activity, "To start rating, Your Lecturer must open the survey.", Toast.LENGTH_LONG).show();
                            } else {
                                Intent mIntent = new Intent(ref_activity, Course_Lacturer_rank.class);
                                String newQuery = mQuery + "/Surveys";
                                mIntent.putExtra("QuerySurvey", newQuery);
                                String[] myDet = {LoginActivity.user_ref.getFaculty(), LoginActivity.user_ref.getInstitution(),
                                        course, Lec_name, semester};
                                mIntent.putExtra("values", myDet);
                                startActivity(mIntent);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //Toast.makeText(ref_activity,"The survey is open",Toast.LENGTH_LONG).show();
            }
        });

    }
}
