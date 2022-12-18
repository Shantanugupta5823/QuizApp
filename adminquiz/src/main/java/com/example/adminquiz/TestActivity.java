package com.example.adminquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.adminquiz.Adapter.TestAdapter;
import com.example.adminquiz.DataBase.DB;

public class TestActivity extends AppCompatActivity {
    private GridView testGrid;
    private CardView addTest;
    private Dialog addTestDialog;
    private EditText addMaxScore, addMaxTime;
    private Button addtestCancel,addTestSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        testGrid = findViewById(R.id.testGrid);
        addTest = findViewById(R.id.addTest);

        Toolbar toolbar = findViewById(R.id.TestPageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DB.g_catList.get(DB.g_selectedSubjectIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        addTestDialog = new Dialog(TestActivity.this);
        addTestDialog.setContentView(R.layout.dialog_add_new_test);
        addTestDialog.setCancelable(false);
        addTestDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        addMaxTime = addTestDialog.findViewById(R.id.testMaxTime);
        addMaxScore = addTestDialog.findViewById(R.id.testMaxScore);
        addtestCancel = addTestDialog.findViewById(R.id.testCancelButton);
        addTestSave = addTestDialog.findViewById(R.id.testSaveButton);

        DB.loadTestData(new myCompleteListener() {
            @Override
            public void onSuccess() {
                TestAdapter testAdapter = new TestAdapter(DB.g_testList);
                testGrid.setAdapter(testAdapter);
            }
            @Override
            public void onFailure() {
                Toast.makeText(TestActivity.this, "Test data couldn't be loaded", Toast.LENGTH_SHORT).show();
            }
        });

        addTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaxTime.setText("");
                addMaxScore.setText("");
                addTestDialog.show();
                  addtestCancel.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          addTestDialog.dismiss();
                      }
                  });

                  addTestSave.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          if(addMaxScore.getText().toString().isEmpty()){
                              addMaxScore.setError("Field can't be empty");
                          }else if(addMaxTime.getText().toString().isEmpty()){
                              addMaxTime.setError("Field can't be Empty");
                          }else {
                              DB.addNewTest(Integer.valueOf(addMaxTime.getText().toString()),Integer.valueOf(addMaxScore.getText().toString()), new myCompleteListener() {
                                  @Override
                                  public void onSuccess() {
                                      testGrid.deferNotifyDataSetChanged();
                                      addTestDialog.dismiss();
                                      Toast.makeText(TestActivity.this, "Test Added Successfully", Toast.LENGTH_SHORT).show();
                                  }
                                  @Override
                                  public void onFailure() {
                                        addTestDialog.dismiss();
                                      Toast.makeText(TestActivity.this, "Sorry,Test Couldn't be Added", Toast.LENGTH_SHORT).show();
                                  }
                              });
                          }
                      }
                  });
            }
        });
        
    }
}