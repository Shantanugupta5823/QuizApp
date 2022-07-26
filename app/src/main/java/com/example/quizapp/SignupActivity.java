package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.content.ContentValues.TAG;

public class SignupActivity extends AppCompatActivity {

    private EditText name, email, password, cnfrmPassword;
    private Button SignupButton;
    private ImageButton backButton;
    private FirebaseAuth mAuth;
    private String emailStr, passStr, cnfrmpassStr , nameStr;
    private Dialog progressDialog;
    private TextView dialogText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        name = findViewById(R.id.SignupName);
        email = findViewById(R.id.SignupEmail);
        password = findViewById(R.id.SignupPassword);
        cnfrmPassword = findViewById(R.id.SignupCnfrmPassword);

        SignupButton = findViewById(R.id.signupSignupButton);
        backButton = findViewById(R.id.backButton);

        progressDialog = new Dialog(SignupActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering User ...");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validate()) {
                    signUpNewUser();
                }
            }
        });
    }
        private void signUpNewUser() {
        progressDialog.show();

            mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                               Toast.makeText(SignupActivity.this,"Signup Successful",Toast.LENGTH_SHORT).show();
                                dbQuery.createUserData(emailStr,nameStr, new myCompleteListner() {
                                    @Override
                                    public void onSuccess() {
                                        dbQuery.loadData(new myCompleteListner() {
                                            @Override
                                            public void onSuccess() {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                                                startActivity(intent);
                                                SignupActivity.this.finish();
                                            }

                                            @Override
                                            public void onFailure() {
                                                progressDialog.dismiss();
                                                Toast.makeText(SignupActivity.this,"Something Went Wrong, Please try Again Later.",
                                                        Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }

                                    @Override
                                    public void onFailure() {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignupActivity.this,"Something Went Wrong, Please try Again Later.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } else {
                                Toast.makeText(SignupActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        }
                    });
        }
        private boolean Validate(){
            nameStr = name.getText().toString().trim();
            passStr = password.getText().toString().trim();
            emailStr = email.getText().toString().trim();
            cnfrmpassStr = cnfrmPassword.getText().toString().trim();

            if(nameStr.isEmpty()){
                name.setError("Enter Your Name");
                return false;
            }
            if(emailStr.isEmpty()){
                email.setError("Enter Email");
                return false;
            }
            if(passStr.isEmpty()){
                password.setError("Enter Password");
                return false;
            }
            if(cnfrmpassStr.isEmpty()){
                cnfrmPassword.setError("Confirm Password");
                return false;
            }
            if(passStr.compareTo(cnfrmpassStr) != 0){
                Toast.makeText(SignupActivity.this,"Password doesn't Match ",Toast.LENGTH_SHORT).show();
                return false;
            }
        return true;
        }

}