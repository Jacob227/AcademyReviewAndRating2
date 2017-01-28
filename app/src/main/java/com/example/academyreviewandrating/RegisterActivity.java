package com.example.academyreviewandrating;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Registration class for sign up to app
 * including user name, full name, pass, faculty, academy, phone, email and more
 */
public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private final String[] our_emails = {"jacob22788@gmail.com", "pitry123@gmail.com"};
    private DatabaseReference mDatabaseFac;
    private DatabaseReference mDatabaseEmail;
    private DatabaseReference mDatabaseLec;
    private String textUserName,textInstitution,textFaculty,textEmail,textPhone,textFullName,textPassword;
    private Spinner faculty_spinner, academy_spinner,semester_spinner,lec_spinner;
    private EditText et_userName, et_code;
    private Boolean textPrivilage;    //TODO
    private FirebaseAuth firebaseAuth;
    private boolean flag_academy,flag_faculty;
    private Random randCode;
    private TextView tv_code;
    private Button bt_reg;
    private Activity myRef;
    private int sizeTop = 20;

    /**
     * Init all registration view components,
     * Init DB reference
     * getting data on faculty, academy and semsters from firebase DB
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("OnCreate"," RegisterActivity before firebase.getInstance()");
        mDatabase = FirebaseDatabase.getInstance().getReference("Academy");
        firebaseAuth = FirebaseAuth.getInstance();
        myRef = this;
        final AutoCompleteTextView ACTextView;
        final RegisterActivity ref_activity = this;
        flag_academy = flag_faculty = false;

        et_userName = (EditText) findViewById(R.id.editTextRegUserName) ;
        faculty_spinner = (Spinner) findViewById(R.id.editTextRegFacl);
        academy_spinner = (Spinner) findViewById(R.id.editTextRegInst);
        semester_spinner = (Spinner) findViewById(R.id.editTextSemester);
        lec_spinner = (Spinner) findViewById(R.id.editTextLecName);
        et_code = (EditText) findViewById(R.id.editTextRegCode);
        tv_code = (TextView) findViewById(R.id.Code_text);
        bt_reg = (Button) findViewById(R.id.buttonRegRegister) ;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> List_inst = new ArrayList<String>();
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    List_inst.add(child.getKey());
                    Log.d("In OnData Register", child.getKey().toString());
                }
                String[] inst_str = new String[List_inst.size()];
                inst_str = List_inst.toArray(inst_str);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, inst_str );
                academy_spinner.setAdapter(adapter);
                flag_academy = true;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        academy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final AdapterView<?> mparent = parent;
                mDatabaseFac = FirebaseDatabase.getInstance().getReference("Academy");//+ parent.getSelectedItem().toString() + "/Faculty");
                mDatabaseFac.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //mparent.getSelectedItem().toString();
                        List<String> List_inst = new ArrayList<String>();
                        List<String> List_Semester = new ArrayList<String>();
                        DataSnapshot mDataSnap = dataSnapshot.child(mparent.getSelectedItem().toString()).child("/Faculty");
                        for (DataSnapshot child: mDataSnap.getChildren()){
                            List_inst.add(child.getKey());
                        }

                        DataSnapshot mDataSnapSemester = dataSnapshot.child(mparent.getSelectedItem().toString()).child("/Semeters");
                        for (DataSnapshot child: mDataSnapSemester.getChildren()){
                            List_Semester.add(child.getKey());
                        }
                        String[] semester_str = new String[List_Semester.size()];
                        semester_str = List_Semester.toArray(semester_str);
                        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                        semester_spinner.setAdapter(adapterSem);

                        String[] inst_str = new String[List_inst.size()];
                        inst_str = List_inst.toArray(inst_str);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, inst_str );
                        faculty_spinner.setAdapter(adapter);
                        flag_faculty = true;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        faculty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mDatabaseLec = FirebaseDatabase.getInstance().getReference("Academy/" + academy_spinner.getSelectedItem().toString()
                        + "/Faculty/" + adapterView.getSelectedItem().toString() + "/Lecturer_faculty");
                mDatabaseLec.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<String> List_Semester = new ArrayList<String>();
                        List_Semester.add("");
                        for (DataSnapshot child: dataSnapshot.getChildren()){
                            List_Semester.add(child.getKey());
                        }
                        String[] semester_str = new String[List_Semester.size()];
                        semester_str = List_Semester.toArray(semester_str);
                        ArrayAdapter<String> adapterSem = new ArrayAdapter<String>(ref_activity, R.layout.my_spinner_item, semester_str );
                        lec_spinner.setAdapter(adapterSem);
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

        lec_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                RadioGroup radioclick = (RadioGroup)findViewById(R.id.RadioGroupRegStudTeac);
                int idPriv = radioclick.getCheckedRadioButtonId();
                RadioButton TempRadio = (RadioButton) findViewById(idPriv);
                String textRadio = TempRadio.getText().toString();

                if (!adapterView.getSelectedItem().toString().equals("") && (textRadio.equals("Teacher")))
                    et_userName.setText(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * clicked on Sign up button
     * getting what user typed
     * create email intent if lecturer clicked, for sending lecturer data
     * to app owner for Confirmation.
     * @param view
     */
    public void RegisterClick(View view) {

        EditText editText;

        textUserName = et_userName.getText().toString().trim();

        if (flag_academy && flag_faculty) {
            textInstitution = academy_spinner.getSelectedItem().toString();
            textFaculty = faculty_spinner.getSelectedItem().toString();
        }
        else {
            Toast.makeText(this, "Wait till the faculty and Academy will loaded", Toast.LENGTH_LONG).show();
            return;
        }

        editText = (EditText) findViewById(R.id.editTextRegEmail);
        textEmail = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.editTextRegPhone);
        textPhone = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.editTextRegFullName);
        textFullName = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.editTextRegPass);
        textPassword = editText.getText().toString().trim();
        RadioGroup radioclick = (RadioGroup)findViewById(R.id.RadioGroupRegStudTeac);
        int idPriv = radioclick.getCheckedRadioButtonId();
        RadioButton TempRadio = (RadioButton) findViewById(idPriv);
        String textRadio = TempRadio.getText().toString();
        if (textRadio.equals("Student")) {
            User user = new User(textUserName, textInstitution, textFaculty, textEmail, textPhone, textFullName, textRadio);
            user.setStarted_semester(semester_spinner.getSelectedItem().toString());
            boolean res = firebaseSignUp(user, true);
        }
        else { //teacher
            LecturerUser user = new LecturerUser(textUserName, textInstitution, textFaculty, textEmail, textPhone, textFullName, textRadio);
            user.setStarted_semester(semester_spinner.getSelectedItem().toString());
            if (!lec_spinner.getSelectedItem().toString().equals("")){ // found
                user.setUserName(lec_spinner.getSelectedItem().toString());
                user.setLec_in_system(true);
            }

            if (et_code.getText().toString().equals("")){
                // send a mail
                if (CheckUserDetails(user)) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, our_emails);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Verify request from " + user.getUserName());
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Name: " + user.getFullName() + "\n"
                            + "Email: " + user.getEmail() + "\nAcademy: " + user.getInstitution() +
                            "\nFaculty: " + user.getFaculty());
                    startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));

                    mDatabaseEmail = FirebaseDatabase.getInstance().getReference("Lecturer_verify");
                    randCode = new Random(); //code number
                    mDatabaseEmail.child(String.valueOf(randCode.nextInt(500000) + 1))
                            .setValue(user);
                } else {
                    return;
                }
            }
            else { //need to verify the code from db
                mDatabaseEmail = FirebaseDatabase.getInstance().getReference("Lecturer_verify");
                final String code_str = et_code.getText().toString();
                mDatabaseEmail.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(code_str)){ //verify code is correct
                            LecturerUser user1 =  dataSnapshot.child(code_str).getValue(LecturerUser.class);
                            firebaseSignUp(user1, false);
                        } else {
                            Toast.makeText(myRef,"Verify code is incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
               // boolean res = firebaseSignUp(user);
            }
        }

    }

    /**
     * Check if all registration data are correct
     * @param user
     * @return
     */
    public boolean CheckUserDetails(User user) {
        //Need to check user name, password, Email.
        if (TextUtils.isEmpty(user.getEmail())) {
            Toast.makeText(this, "Please enter mail", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(textPassword)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(user.getUserName())) {
            Toast.makeText(this, "Please enter User name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    /**
     * Sign up the user after all checking, using firebaseAuth API
     * @param user
     * @param checkDetails
     * @return
     */
    public boolean firebaseSignUp(User user, boolean checkDetails){

        if (checkDetails) {
            CheckUserDetails(user);
        }

        final User userRef = user;
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), textPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){    //TODO: check all exception of Weak password in registartion
                    final FirebaseUser firebaseuser = firebaseAuth.getCurrentUser();  //need for uid
                    mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                    mDatabase.child(firebaseuser.getUid()).setValue(userRef);
                    LoginActivity.user_ref = new User(userRef);
                    LoginActivity.user_created = true;
                    Toast.makeText(RegisterActivity.this,"Registrered Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
                    finish();

                }
                else{
                    Toast.makeText(RegisterActivity.this,"Could not register.. please try again.",Toast.LENGTH_LONG).show();
                }
            }
        });



        return true;
    }


}
