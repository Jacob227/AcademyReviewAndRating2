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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = (EditText) findViewById(R.id.editTextUser);
        password = (EditText) findViewById(R.id.editTextPass);
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("OnCreate LoginActivity","before firebase.getcurrentUser()");
        if(firebaseAuth.getCurrentUser() != null){
            //User is already loged in
            Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
        }

    }

    public void LoginClick(View view){

        if (TextUtils.isEmpty(email.getText().toString()) ){
            Toast.makeText(this,"Please enter mail",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString()) ){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        String ff = email.getText().toString().trim();
        String ee = password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Login Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),NavigationStartActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Could login.. please try again or register",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void RegisterClick(View view){
        Log.d("RegisterClick","In Register click");
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
