package com.example.adminquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminquiz.Adapter.SubjectsAdapter;
import com.example.adminquiz.DataBase.DB;
import com.google.firebase.auth.FirebaseAuth;

public class SubjectsPage extends AppCompatActivity {
    private GridView grid;
    private CardView addSubject;
    private Dialog addSubDialog,logoutDialog;
    private TextView logoutText;
    private EditText newSub;
    private Button cancel,save,logout,logoutYes,logoutNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_page);

        grid = findViewById(R.id.categoryGrid);
        addSubject = findViewById(R.id.addSubject);

        Toolbar toolbar = findViewById(R.id.subjectPageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ADMIN PANEL");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout = findViewById(R.id.logoutBtn);

        logoutDialog = new Dialog(SubjectsPage.this);
        logoutDialog.setContentView(R.layout.delete_dialog_test);
        logoutDialog.setCancelable(false);
        logoutDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        logoutNo = logoutDialog.findViewById(R.id.noButton);
        logoutYes = logoutDialog.findViewById(R.id.yesButton);
        logoutText = logoutDialog.findViewById(R.id.justtext);
        logoutText.setText("You Really want to logout");

        addSubDialog = new Dialog(SubjectsPage.this);
        addSubDialog.setContentView(R.layout.add_dialog_layout);
        addSubDialog.setCancelable(false);
        addSubDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        newSub =  addSubDialog.findViewById(R.id.newSubjectName);
        cancel = addSubDialog.findViewById(R.id.cancelButton);
        save = addSubDialog.findViewById(R.id.saveButton);

        SubjectsAdapter adapter = new SubjectsAdapter(DB.g_catList);
        grid.setAdapter(adapter);

        addSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSubDialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addSubDialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         if(newSub.getText().toString().isEmpty()) {
                             newSub.setError("Enter a name");
                         }else{
                             DB.addNewSubject(newSub.getText().toString(), new myCompleteListener() {
                                 @Override
                                 public void onSuccess() {
                                     grid.deferNotifyDataSetChanged();
                                     addSubDialog.dismiss();
                                 }
                                 @Override
                                 public void onFailure() {
                                     addSubDialog.dismiss();
                                     Toast.makeText(SubjectsPage.this, "Sorry couldn't add new Subject", Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }
                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutDialog.show();
                logoutYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDialog.dismiss();
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(SubjectsPage.this,LoginActivity.class);
                        startActivity(intent);
                        SubjectsPage.this.finish();
                    }
                });

                logoutNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        logoutDialog.dismiss();
                    }
                });

            }
        });

    }
}