package com.example.academyreviewandrating;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.CourseDetailsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Add course to the system (only Lecturer permission)
 */
public class AddCourse extends Fragment {

    private Activity myAct;
    private EditText et_course_name, et_code, et_credits, et_time, et_room, et_syllabus, et_Lec_name;
    private Spinner sp_semester;
    private Button bt_add;
    private DatabaseReference databaseReference;
    private ArrayList<String> mSpinnerArray;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private ArrayAdapter<String> mAdapter;

    private AlphaAnimation buttonClick = new AlphaAnimation(Animation.ZORDER_BOTTOM,Animation.ZORDER_NORMAL);
    View myView;
    public AddCourse() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.activity_add_course,container,false);
        Log.d("onCreateView ","NavigationSurvey");
        return myView;
    }

    /**
     * Initialize all the components including
     * the DB reference.
     * Initialize button listener.
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myAct = getActivity();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        et_course_name = (EditText) getView().findViewById(R.id.Code_course_Name_text2);
        et_code = (EditText) getView().findViewById(R.id.Code_course_text2);
        et_credits = (EditText) getView().findViewById(R.id.Credits_text2);
        et_time = (EditText) getView().findViewById(R.id.Time_text2);
        et_room = (EditText) getView().findViewById(R.id.Room_text2);
        et_syllabus = (EditText) getView().findViewById(R.id.Syllabus_text2);
        et_Lec_name = (EditText) getView().findViewById(R.id.Lec_name_text2);
        et_Lec_name.setText(LoginActivity.user_ref.getUserName());
        mSpinnerArray = new ArrayList<String>();
        sp_semester = (Spinner) getView().findViewById(R.id.spinner_semester);
        bt_add = (Button)getView().findViewById(R.id.button_add);
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                if (TextUtils.isEmpty(et_course_name.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter Couyrse name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_code.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter code course",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_time.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter time",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_room.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter room",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_syllabus.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter syllabus",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(et_credits.getText().toString()) ){
                    Toast.makeText(myAct,"Please enter credits",Toast.LENGTH_SHORT).show();
                    return;
                }

                databaseReference = FirebaseDatabase.getInstance().getReference("Academy/" + LoginActivity.user_ref.getInstitution());
                databaseReference.child("Lecturer").child(et_Lec_name.getText().toString()).child("Courses").
                        child(et_course_name.getText().toString()).setValue(et_code.getText().toString());

                CourseDetailsModel courseDetailsModel = new CourseDetailsModel(et_room.getText().toString(),et_syllabus.getText().toString(),
                        et_time.getText().toString(),Float.parseFloat(et_credits.getText().toString()),
                        Float.parseFloat(et_code.getText().toString()));
                courseDetailsModel.setCourse_name(et_course_name.getText().toString());

               // Log.d("yoooo", "Faculty/" + LoginActivity.user_ref.getFaculty() + "/Lecturer_faculty/" +
                 //       et_Lec_name.getText().toString() + "/Courses/" + et_course_name + "/Semesters");

                databaseReference.child("Faculty/" + LoginActivity.user_ref.getFaculty() + "/Lecturer_faculty/" +
                        et_Lec_name.getText().toString() + "/Courses/" + et_course_name.getText().toString() + "/Semesters/"
                 + sp_semester.getSelectedItem().toString()).setValue(1);


                databaseReference.child("Faculty/" + LoginActivity.user_ref.getFaculty() + "/Lecturer_faculty/" +
                        et_Lec_name.getText().toString() + "/Courses/" + et_course_name.getText().toString() + "/code_course").
                        setValue(et_code.getText().toString());

                Log.d("yooo", "Faculty/" + LoginActivity.user_ref.getFaculty() + "/Course/"
                        + et_course_name.getText().toString() + "/" + sp_semester.getSelectedItem().toString() +
                        "/Course Details");

                databaseReference.child("Faculty/" + LoginActivity.user_ref.getFaculty() + "/Course/"
                + et_course_name.getText().toString() + "/" + sp_semester.getSelectedItem().toString() +
                "/Course Details" ).setValue(courseDetailsModel);

                databaseReference.child("Faculty/" + LoginActivity.user_ref.getFaculty() + "/Course/"
                        + et_course_name.getText().toString() + "/" + sp_semester.getSelectedItem().toString() +
                        "/Lecturer/" + et_Lec_name.getText().toString() ).setValue("Rating");
                LoginActivity.user_ref.setLec_in_system(true);
                Toast.makeText(myAct,"Thank you for adding a new course, now you need to wait till someone will rating you :)",Toast.LENGTH_LONG).show();

            }
        });

        //sp_semester.
                databaseReference = FirebaseDatabase.getInstance().getReference("Academy/"
                + LoginActivity.user_ref.getInstitution() + "/Faculty/" +
                LoginActivity.user_ref.getFaculty() + "/User_course");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap_semsters: dataSnapshot.getChildren()){
                    mSpinnerArray.add(snap_semsters.getKey());
                }
                String[] spinner_str = new String[mSpinnerArray.size()];
                int i = 0;
                for (String key : mSpinnerArray) {
                    spinner_str[i] = key;
                    i++;
                }

                mAdapter = new ArrayAdapter<String>(myAct,
                        android.R.layout.simple_spinner_item, spinner_str);
                sp_semester.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
