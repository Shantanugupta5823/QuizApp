package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;


import com.example.quizapp.Adapter.AnswersAdapter;
import com.example.quizapp.DataBase.dbQuery;

public class AnswersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView aaRecycle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        toolbar = findViewById(R.id.aa_toolbar);
        aaRecycle = findViewById(R.id.aa_RecyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(dbQuery.g_catList.get(dbQuery.g_selectedCatIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        aaRecycle.setLayoutManager(linearLayoutManager);

        AnswersAdapter adapter = new AnswersAdapter(dbQuery.g_questionList);
        aaRecycle.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            AnswersActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}