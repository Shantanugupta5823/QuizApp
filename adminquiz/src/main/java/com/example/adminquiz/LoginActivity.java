package com.example.adminquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adminquiz.DataBase.DB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPassword, loginKey;
    Button loginButton;
    private FirebaseAuth mAuth;
    private int backpress;
    private int currentApiVersion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginKey = findViewById(R.id.personalKey);
        loginButton = findViewById(R.id.loginLoginButton);

        mAuth = FirebaseAuth.getInstance();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateData()){
                    login();
                }
            }
        });


    }

    private boolean validateData() {
        if((loginEmail.getText().toString().isEmpty())){
            Toast.makeText(LoginActivity.this,"Email Not provided",Toast.LENGTH_SHORT).show();
            return false;
        }else if(loginPassword.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Password Not provided",Toast.LENGTH_SHORT).show();
            return false;
        }else if(loginKey.getText().toString().isEmpty()){
            Toast.makeText(LoginActivity.this,"Key Not provided",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void login() {

        String emailStr = loginEmail.getText().toString().trim();
        String passStr = loginPassword.getText().toString().trim();
        String keyStr = loginKey.getText().toString().trim();

        if(keyStr.equals("4564")){
            mAuth.signInWithEmailAndPassword("shantanugupta471@gmail.com", "Shan@123")
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(LoginActivity.this,"Login Success",Toast.LENGTH_SHORT).show();
                                DB.loadCategories(new myCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                        Intent intent = new Intent(LoginActivity.this,SubjectsPage.class);
                                        startActivity(intent);
                                        LoginActivity.this.finish();
                                    }
                                    @Override
                                    public void onFailure() {
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }else{
                                Toast.makeText(LoginActivity.this, "No Success", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }else{
            Toast.makeText(LoginActivity.this, "Not a teacher, close this application", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        backpress++;
        Toast.makeText(this, "Press again to leave", Toast.LENGTH_SHORT).show();
        if(backpress>1){
            this.finish();
        }
    }
}