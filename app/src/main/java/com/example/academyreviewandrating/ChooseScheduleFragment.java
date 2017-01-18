package com.example.academyreviewandrating;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ScrollingTabContainerView;
import android.text.TextUtils;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.example.academyreviewandrating.Model.rating_lecterer_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChooseScheduleFragment extends Fragment {

    private DatabaseReference databaseReference;
    private Spinner spinner_semester;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> mSpinnerArray;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private HashMap<String,ArrayList<CourseDetailsModel>> mapCourseDet;
    private View myView;
    private Activity myAct;
    private Button table_butt;
    private Button list_butt;
    private TextView faculty_text, academy_text;

    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);

    public ChooseScheduleFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_choose_schedule,container,false);
        return myView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner_semester = (Spinner) getView().findViewById(R.id.spinner_choose_semester);
        mapCourseDet = new HashMap<String,ArrayList<CourseDetailsModel>>();
        faculty_text = (TextView) getView().findViewById(R.id.Faculty_text2) ;
        academy_text = (TextView) getView().findViewById(R.id.Academy_text2) ;

        myAct = getActivity();
        mSpinnerArray = new ArrayList<String>();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (LoginActivity.user_ref != null) {
            academy_text.setText(LoginActivity.user_ref.getInstitution());
            faculty_text.setText(LoginActivity.user_ref.getFaculty());
            databaseReference = FirebaseDatabase.getInstance().getReference("Academy/"
                    + LoginActivity.user_ref.getInstitution() + "/Faculty/" +
                    LoginActivity.user_ref.getFaculty() + "/User_course");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap_semsters: dataSnapshot.getChildren()){
                        mSpinnerArray.add(snap_semsters.getKey());
                        DataSnapshot snap_byUserId = snap_semsters.child(firebaseUser.getUid());
                        for (DataSnapshot snap_courses: snap_byUserId.getChildren()){ //brings all the courses that the user has been registered
                            CourseDetailsModel courseDetailsModel = snap_courses.getValue(CourseDetailsModel.class);
                            courseDetailsModel.setCourse_name(snap_courses.getKey());
                            if (mapCourseDet.get(snap_semsters.getKey()) == null) {
                                mapCourseDet.put(snap_semsters.getKey(), new ArrayList<CourseDetailsModel>());
                            }
                            mapCourseDet.get(snap_semsters.getKey()).add(courseDetailsModel);
                        }
                    }
                    String[] spinner_str = new String[mSpinnerArray.size()];
                    int i = 0;
                    for (String key : mSpinnerArray) {
                        spinner_str[i] = key;
                        i++;
                    }

                    mAdapter = new ArrayAdapter<String>(myAct,
                            android.R.layout.simple_spinner_item, spinner_str);
                    spinner_semester.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(myAct, "No connection to internet", Toast.LENGTH_LONG).show();
        }

        table_butt = (Button)getView().findViewById(R.id.choose_semster_but);
        table_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(buttonClick);
                String mSelected = spinner_semester.getSelectedItem().toString();
                if ( TextUtils.isEmpty(mSelected))
                    Toast.makeText(myAct,"You must choose some semster",Toast.LENGTH_LONG).show();
                else {
                    Intent mIntend = new Intent(myAct,ScheduleTable.class);
                    mIntend.putExtra("values",mSelected);
                    ArrayList<CourseDetailsModel> toIntend = mapCourseDet.get(mSelected);
                    if (toIntend == null){
                        Toast.makeText(myAct,"You are not signed to any course on this semester",Toast.LENGTH_LONG).show();
                    } else {
                        mIntend.putExtra("arraylistCourse", toIntend);
                        startActivity(mIntend);
                    }
                }
            }
        });

        list_butt = (Button)getView().findViewById(R.id.choose_semster_but_list);
        list_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setAnimation(buttonClick);
                String mSelected = spinner_semester.getSelectedItem().toString();
                if ( TextUtils.isEmpty(mSelected))
                    Toast.makeText(myAct,"You must choose some semster",Toast.LENGTH_LONG).show();
                else {
                    Intent mIntend = new Intent(myAct,listViewCourseList.class);
                    mIntend.putExtra("values",mSelected);
                    ArrayList<CourseDetailsModel> toIntend = mapCourseDet.get(mSelected);
                    if (toIntend == null){
                        Toast.makeText(myAct,"You are not signed to any course on this semester",Toast.LENGTH_LONG).show();
                    } else {
                        mIntend.putExtra("arraylistCourse", toIntend);
                        startActivity(mIntend);
                    }
                }
            }
        });

    }

}
