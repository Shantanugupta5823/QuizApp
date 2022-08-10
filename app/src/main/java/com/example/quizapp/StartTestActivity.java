package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.DataBase.dbQuery;

import static com.example.quizapp.DataBase.dbQuery.g_catList;
import static com.example.quizapp.DataBase.dbQuery.g_testList;

public class StartTestActivity extends AppCompatActivity {
    private Button startTestButton;
    private ImageView back_button;
    private TextView catName , testNo, totalQue , totalScore , time,  dialogtext;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        init();

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");
        progressDialog.show();

        dbQuery.loadQuestions(new myCompleteListner() {
            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this,"Something Went Wrong, Please try Again Later.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void init(){
        back_button = findViewById(R.id.startTest_backButton);
        startTestButton = findViewById(R.id.startTestButton);
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_number);
        totalQue = findViewById(R.id.st_total_ques);
        totalScore = findViewById(R.id.st_max_score);
        time = findViewById(R.id.st_max_time);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        startTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this,QuestionActivity.class);
                startActivity(intent);
                StartTestActivity.this.finish();
            }
        });
    }
    private void setData(){
        catName.setText(g_catList.get(dbQuery.g_selectedCatIndex).getName());
        testNo.setText("Test No. " + String.valueOf(dbQuery.g_SelectedTestIndex+1));
        totalQue.setText(String.valueOf(dbQuery.g_questionList.size()));
        totalScore.setText(String.valueOf(g_testList.get(dbQuery.g_SelectedTestIndex).getTopScore()));
        time.setText(String.valueOf(g_testList.get(dbQuery.g_SelectedTestIndex).getTime()));

    }
}