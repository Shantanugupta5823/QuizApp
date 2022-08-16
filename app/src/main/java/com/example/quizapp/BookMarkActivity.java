package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapter.AnswersAdapter;
import com.example.quizapp.Adapter.BookMarkAdapter;
import com.example.quizapp.DataBase.dbQuery;

public class BookMarkActivity extends AppCompatActivity {

    private RecyclerView questionsView;
    private Toolbar toolbar;
    private Dialog progressDialog;
    private TextView dialogtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        toolbar = findViewById(R.id.bm_toolbar);
        questionsView = findViewById(R.id.bm_RecyclerView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new Dialog(BookMarkActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        questionsView.setLayoutManager(linearLayoutManager);

        dbQuery.loadBookmarks(new myCompleteListner() {
            @Override
            public void onSuccess() {
                BookMarkAdapter adapter = new BookMarkAdapter(dbQuery.g_bookmarksList);
                questionsView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(BookMarkActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
           BookMarkActivity.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}