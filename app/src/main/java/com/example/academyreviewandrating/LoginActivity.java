package com.example.academyreviewandrating;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.academyreviewandrating.Model.LecturerUser;
import com.example.academyreviewandrating.Model.User;
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

/**
 * start activity class, main class that load when
 * the app load, can sign in to app, also can register to app
 */
public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatebase;
    static  DatabaseReference static_db_ref;
    static FirebaseUser firebaseUser = null;
    static User user_ref = null;
    static boolean user_created = false;
    static String current_semester = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.editTextUser);
        password = (EditText) findViewById(R.id.editTextPass);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Log.d("OnCreate LoginActivity","before firebase.getcurrentUser()");
        if(firebaseUser != null){
            //User is already loged in
            Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
            createUserInstance();
            startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
            finish();
        }

    }

    /**
     * Create a user static reference to avoid delay on app
     * when we want to get user details from app
     */
    public void createUserInstance() {
        static_db_ref = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        Log.d("In createUserInstance","before addValueEventListener");
        static_db_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String[] temp_string = new String[20];
                int i = 0;
                current_semester = "2016_a";
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    temp_string[i] = child.getValue().toString();
                    i++;
                    //user_ref = (User)child.getValue();
                }
               // user_ref = new User(temp_string[6], temp_string[3],temp_string[1],temp_string[0],temp_string[4],temp_string[2],temp_string[5]);
                if (temp_string[6].equals("Student")) {
                    user_ref = dataSnapshot.getValue(User.class);
                }
                else {
                    user_ref = dataSnapshot.getValue(LecturerUser.class);
                }
                user_created = true;
                if (NavigationStartActivity.mTV != null){
                    NavigationStartActivity.mTV.setText(user_ref.getUserName() + "\n  " + user_ref.getFaculty()
                             + ", " + user_ref.getInstitution());
                     Log.d("NavBar", "After mtv");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * When user clicked on login button.
     * check if password and email are filled.
     * Using firebaseAuth for sign into app
     * @param view
     */
    public void LoginClick(View view){

        if (TextUtils.isEmpty(email.getText().toString()) ){
            Toast.makeText(this,"Please enter mail",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString()) ){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser = firebaseAuth.getCurrentUser();
                    createUserInstance();

                    startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginActivity.this,"Could login.. please try again or register",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * When user clicked on Register
     * open a new register activity
     * @param view
     */
    public void RegisterClick(View view){
        Log.d("RegisterClick","In Register click");
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
