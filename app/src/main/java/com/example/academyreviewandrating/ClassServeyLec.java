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

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClassServeyLec extends Fragment {

    View myView;
    private Button bt_stop, bt_start, bt_watch_rev;
    private Spinner sp_semester,sp_course;
    private DatabaseReference firebaseDatabase;
    private Activity ref_activity;
    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    private String query = "Academy/" +
            LoginActivity.user_ref.getInstitution() + "/Faculty/"
            + LoginActivity.user_ref.getFaculty() + "/Lecturer_faculty/" +
            LoginActivity.user_ref.getUserName() + "/Courses/";

    private Boolean bt_startPressed = false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_class_servey_lec,container,false);
        Log.d("onCreateView ","NavigationSurvey");
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bt_start = (Button) getView().findViewById(R.id.choose_start_but);
        bt_stop = (Button) getView().findViewById(R.id.choose_stop_but);
        bt_watch_rev = (Button) getView().findViewById(R.id.choose_watch_rev);
        ref_activity = getActivity();
        sp_semester = (Spinner) getView().findViewById(R.id.spinner_sem);
        sp_course = (Spinner) getView().findViewById(R.id.spinner_course);

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
                sp_course.setAdapter(adapterSem);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sp_course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                final AdapterView<?> fadapterView = adapterView;
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(query +
                        fadapterView.getSelectedItem().toString() + "/Semesters");
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
                String course = sp_course.getSelectedItem().toString();
                String semester = sp_semester.getSelectedItem().toString();
                String mQuery = query + course + "/Semesters/" + semester + "/Survey Act";
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(mQuery);
                firebaseDatabase.setValue(true);
                Toast.makeText(ref_activity,"The survey is open",Toast.LENGTH_LONG).show();
            }
        });

        bt_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bt_startPressed) {
                    view.setAnimation(buttonClick);
                    String course = sp_course.getSelectedItem().toString();
                    String semester = sp_semester.getSelectedItem().toString();
                    String mQuery = query + course + "/Semesters/" + semester + "/Survey Act";
                    firebaseDatabase = FirebaseDatabase.getInstance().getReference(mQuery);
                    firebaseDatabase.setValue(false);
                    Toast.makeText(ref_activity, "The survey closed", Toast.LENGTH_LONG).show();
                    bt_startPressed = true;
                }
                else {
                    Toast.makeText(ref_activity, "You must open survey first", Toast.LENGTH_LONG).show();
                }
            }
        });

        bt_watch_rev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setAnimation(buttonClick);
                final String[] mes = {LoginActivity.user_ref.getFaculty(),LoginActivity.user_ref.getInstitution(),
                        sp_course.getSelectedItem().toString(), LoginActivity.user_ref.getUserName()};

                String NewQuery = query + mes[2] + "/Semesters/" + sp_semester.getSelectedItem().toString();
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(NewQuery);

                firebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ArrayList<rating_lecterer_model> mLecSurvey = new ArrayList<rating_lecterer_model>();
                        if (dataSnapshot.hasChild("Surveys")){
                                DataSnapshot survey = dataSnapshot.child("Surveys");
                                for (DataSnapshot LecSurveys: survey.getChildren()){
                                    mLecSurvey.add(LecSurveys.getValue(rating_lecterer_model.class));
                                }
                            Intent mIndent = new Intent(ref_activity,WatchReviews.class);
                            mIndent.putExtra("Rating",mLecSurvey );
                            mIndent.putExtra("values", mes);
                            mIndent.putExtra("classSurvey", "mySurvey");
                            startActivity(mIndent);
                        } else {
                            Toast.makeText(ref_activity,"You havn't rated.",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}
