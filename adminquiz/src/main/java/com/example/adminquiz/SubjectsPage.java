package com.example.adminquiz;

import androidx.appcompat.app.AppCompatActivity;
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

public class SubjectsPage extends AppCompatActivity {
    private GridView grid;
    private CardView addSubject;
    private Dialog addSubDialog;
    EditText newSub;
    Button cancel,save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_page);

        grid = findViewById(R.id.categoryGrid);
        addSubject = findViewById(R.id.addSubject);

        addSubDialog = new Dialog(SubjectsPage.this);
        addSubDialog.setContentView(R.layout.add_dialog_layout);
        addSubDialog.setCancelable(false);
        addSubDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
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
                        DB.addNewSubject(newSub.getText().toString().trim().toUpperCase(), new myCompleteListener() {
                            @Override
                            public void onSuccess() {
                                DB.loadCategories(new myCompleteListener() {
                                    @Override
                                    public void onSuccess() {
                                         adapter.notifyDataSetChanged();
                                        addSubDialog.dismiss();
                                    }

                                    @Override
                                    public void onFailure() {

                                    }
                                });

                            }
                            @Override
                            public void onFailure() {

                            }
                        });
                    }
                });
            }
        });







    }
}