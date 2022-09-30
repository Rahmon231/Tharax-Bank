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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lemzeeyyy.tharaxbank.Model.MyUsers;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private EditText nameET, emailEt, phoneET, passwordET;
    private Button signUpBtn;
    private TextView addressTV;
    FirebaseAuth auth;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initWidgets();
       auth = FirebaseAuth.getInstance();
        signUpBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString().trim();
            String password = passwordET.getText().toString().trim();
            String phone = phoneET.getText().toString().trim();
            String userName = nameET.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                emailEt.setError("Please Enter an email");
            }
            if(TextUtils.isEmpty(password)){
                passwordET.setError("Please enter password");
            }
            if(TextUtils.isEmpty(phone)){
                phoneET.setError("Please enter phone number");
            }
            if(TextUtils.isEmpty(userName)){
                passwordET.setError("Please enter username");
            }
            if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
                    && !TextUtils.isEmpty(phone) && !TextUtils.isEmpty(userName)){
                registerUser(email,password,userName,phone);
            }
        });
    }

    private void registerUser(String email, String password, final String userName, final String phone) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       FirebaseUser user = auth.getCurrentUser();
                       String userId = user.getUid();
                       myRef = FirebaseDatabase.getInstance()
                               .getReference("MyUsers")
                               .child(userId);
                       HashMap<String, Object> userInfo = new HashMap<>();
                       userInfo.put("id", userId);
                       userInfo.put("userName",userName);
                       userInfo.put("phone",phone);
                       userInfo.put("email",email);
                       myRef.setValue(userInfo)
                               .addOnCompleteListener(task1 -> {
                              if(task1.isSuccessful()){
                                  Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                  startActivity(intent);
                                  finish();
                              }else {
                                  Toast.makeText(SignUpActivity.this, task1.getException().toString(), Toast.LENGTH_SHORT).show();
                              }
                               });
                   }else {
                       Toast.makeText(SignUpActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                       Log.d("TAG", "onComplete: "+task.getException().toString());
                   }
                });
    }

    private void initWidgets() {
        nameET = findViewById(R.id.edittext_id_name_sign_up);
        emailEt = findViewById(R.id.email_sign_up);
        phoneET = findViewById(R.id.edittext_phone_sign_up);
        passwordET = findViewById(R.id.edittext_user_password_sign_up);
        addressTV = findViewById(R.id.signup_address);
        signUpBtn = findViewById(R.id.button_sign_up);
    }
}