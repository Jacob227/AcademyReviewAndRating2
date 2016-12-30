package com.example.academyreviewandrating;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class RegisterActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String textUserName,textInstitution,textFaculty,textEmail,textPhone,textFullName,textPassword;
    private Boolean textPrivilage;    //TODO
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("OnCreate"," RegisterActivity before firebase.getInstance()");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void RegisterClick(View view) {
        EditText editText;
        editText = (EditText) findViewById(R.id.editTextRegUserName);
        textUserName = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.editTextRegInst);
        textInstitution = editText.getText().toString().trim();
        editText = (EditText) findViewById(R.id.editTextRegFacl);
        textFaculty = editText.getText().toString().trim();
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
        User user = new User(textUserName,textInstitution,textFaculty,textEmail,textPhone,textFullName,textRadio);
        boolean res = checkAllRegisterDetails(user);

    }

    public boolean checkAllRegisterDetails(User user){
        //Need to check user name, password, Email.
        if (TextUtils.isEmpty(user.getEmail()) ){
            Toast.makeText(this,"Please enter mail",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(textPassword)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(user.getUserName())) {
            Toast.makeText(this, "Please enter User name", Toast.LENGTH_SHORT).show();
            return false;
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
                    finish();
                    startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
                }
                else{
                    Toast.makeText(RegisterActivity.this,"Could register.. please try again.",Toast.LENGTH_LONG).show();
                }
            }
        });



        return true;
    }
}
