package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.DataBase.dbQuery;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {
    private TextView scoreTV, timeTV , totalQTV , correctQTV , wrongQTV , unattemptedQTV,dialogtext;
    private Button leaderBtn , reAttemptBtn, viewAnsBtn;
    private long timeTaken;
    private Dialog progressDialog;
    private Toolbar toolbar2;
    private int finalScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        toolbar2 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");
        progressDialog.show();

        init();
        loadData();
        reAttemptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });
        viewAnsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        saveResult();
    }
    private void saveResult() {
            dbQuery.saveResult(finalScore, new myCompleteListner() {
                @Override
                public void onSuccess() {
                    progressDialog.dismiss();
                }
                @Override
                public void onFailure() {
                    Toast.makeText(ScoreActivity.this,"Something Went Wrong, please try again later",Toast.LENGTH_SHORT);
                    progressDialog.dismiss();
                }
            });
    }
    private void init(){
        scoreTV = findViewById(R.id.finalScore);
        timeTV = findViewById(R.id.timeTaken);
        totalQTV = findViewById(R.id.totalQues);
        correctQTV = findViewById(R.id.correct);
        wrongQTV = findViewById(R.id.wrong);
        unattemptedQTV = findViewById(R.id.un_attempted);
        leaderBtn = findViewById(R.id.leaderboardButton);
        reAttemptBtn = findViewById(R.id.reAttemptButton);
        viewAnsBtn = findViewById(R.id.viewAnsButton);
    }
    private void loadData(){

        int correctQ = 0, wrongQ = 0, unattemptedQ = 0;
        for(int i = 0; i< dbQuery.g_questionList.size(); i++){
            if(dbQuery.g_questionList.get(i).getSelectedAns() == -1){
                unattemptedQ++;
            }else if(dbQuery.g_questionList.get(i).getSelectedAns() ==  dbQuery.g_questionList.get(i).getCorrectAns()){
                correctQ++;
            }else{
                wrongQ++;
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptedQ));
        totalQTV.setText(String.valueOf(dbQuery.g_questionList.size()));

        finalScore = (correctQ*100)/dbQuery.g_questionList.size();
        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("Time Taken",0);
        String time = String.format("%02d:%02d min"
                , TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));
        timeTV.setText(time);
    }
    private void reAttempt(){
        for(int i = 0 ; i<dbQuery.g_questionList.size(); i++){
            dbQuery.g_questionList.get(i).setStatus(dbQuery.NOT_VISITED);
            dbQuery.g_questionList.get(i).setSelectedAns(-1);
        }
        Intent intent = new Intent(ScoreActivity.this, StartTestActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ScoreActivity.this,TestActivity.class);
        startActivity(intent);
        finish();
    }

}