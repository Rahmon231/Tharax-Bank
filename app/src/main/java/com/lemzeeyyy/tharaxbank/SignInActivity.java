package com.lemzeeyyy.tharaxbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email_idNumberET;
    private EditText passwordET;
    private Button sign_in;
    private Button sign_up;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initWidgets();
        auth = FirebaseAuth.getInstance();

    }

    private void initWidgets() {
        email_idNumberET = findViewById(R.id.edittext_id_number_sign_in);
        passwordET = findViewById(R.id.edittext_user_password_sign_in);
        sign_in = findViewById(R.id.button_sign_in);
        sign_up = findViewById(R.id.btn_sign_up);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_sign_in:

                email_idNumberET = findViewById(R.id.edittext_id_number_sign_in);
                String email_idNumber = email_idNumberET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();

                if(TextUtils.isEmpty(email_idNumber)){
                    email_idNumberET.setError("Please Enter an email/id number");
                }
                if(TextUtils.isEmpty(password)){
                    passwordET.setError("Please enter password");
                }
                if(!TextUtils.isEmpty(email_idNumber) && !TextUtils.isEmpty(password)){
                    signInUser(email_idNumber,password);
                }
                break;
            case R.id.button_sign_up:
                startActivity(new Intent(SignInActivity.this,SignUpActivity.class));
                break;
        }
    }

    private void signInUser(String email_idNumber, String password) {
        auth.signInWithEmailAndPassword(email_idNumber,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(SignInActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            startActivity(new Intent(SignInActivity.this,MainActivity.class));
            finish();
        }
    }
}