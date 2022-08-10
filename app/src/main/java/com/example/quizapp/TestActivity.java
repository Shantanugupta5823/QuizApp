package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapter.TestAdapter;
import com.example.quizapp.DataBase.dbQuery;

public class TestActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView testRecycle;
    private TestAdapter adapter;
    private Dialog progressDialog;
    private TextView dialogtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(dbQuery.g_catList.get(dbQuery.g_selectedCatIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");

        progressDialog.show();

        testRecycle = findViewById(R.id.testRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        testRecycle.setLayoutManager(linearLayoutManager);

        dbQuery.loadTestData(new myCompleteListner() {
            @Override
            public void onSuccess() {
                dbQuery.loadMyScore(new myCompleteListner() {
                    @Override
                    public void onSuccess() {
                        TestAdapter adapter = new TestAdapter(dbQuery.g_testList);
                        testRecycle.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(TestActivity.this,"Something Went Wrong, Please try Again Later.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this,"Something Went Wrong, Please try Again Later.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            TestActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}