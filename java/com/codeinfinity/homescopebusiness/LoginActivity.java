package com.codeinfinity.homescopebusiness;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView register, forgotpass;
    Button login;
    FirebaseAuth mAuth;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registerText);
        forgotpass = findViewById(R.id.forgotPassText);
        email = findViewById(R.id.emailEdit);
        password = findViewById(R.id.passEdit);
        login = findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyDetails();
            }
        });


    }

    public void verifyDetails(){

        String emaill = email.getText().toString();
        String passwordl = password.getText().toString();

        if (emaill.isEmpty() || !emaill.contains("@gmail.com")){
            email.setError("Invalid email!");
        } else if (passwordl.isEmpty()) {
            password.setError("Password can't be empty!");
        }else {
            signinUser();
        }

    }

    public void signinUser(){
        String emaill = email.getText().toString();
        String passwordl = password.getText().toString();
        mAuth.signInWithEmailAndPassword(emaill, passwordl)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            Toast.makeText(LoginActivity.this, task.getException().toString(),
                                    Toast.LENGTH_LONG).show();

                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

}