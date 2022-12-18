package com.example.adminquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.adminquiz.DataBase.DB;

public class AddQuestionsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView testNo,noOfQuestions,quesNo;
    private Button reviewTest,addQuestionButton;
    private EditText maxTime,totalScore,question,optionA,optionB,optionC,optionD,correctOption;
    private ImageButton addNewQuestionButton;
    private LinearLayout newQuesLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_questions);
        init();

        addNewQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newQuesLayout.setVisibility(View.VISIBLE);
            }
        });
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String Question = question.getText().toString();
                    String A = optionA.getText().toString();
                    String B = optionB.getText().toString();
                    String C = optionC.getText().toString();
                    String D = optionD.getText().toString();
                    Long correct = Long.valueOf(correctOption.getText().toString());
                    DB.addNewQuestion(Question, A, B, C, D, correct, new myCompleteListener() {
                        @Override
                        public void onSuccess() {
                            question.setText("");optionA.setText("");optionB.setText("");
                            optionC.setText("");optionD.setText("");correctOption.setText("");
                            noOfQuestions.setText("Questions : "+ String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getNoOfQuestions()));
                            quesNo.setText("Questions No. : "+ String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getNoOfQuestions()+1));
                            newQuesLayout.setVisibility(View.GONE);
                            Toast.makeText(AddQuestionsActivity.this, "Question added Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(AddQuestionsActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        reviewTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.loadQuestions(new myCompleteListener() {
                    @Override
                    public void onSuccess() {
                        Intent intent = new Intent(AddQuestionsActivity.this,ReviewQuestions.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure() {
                        Toast.makeText(AddQuestionsActivity.this, "Couldn't be loaded,sorry", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });
    }

    private boolean validate() {
        if(question.getText().toString().isEmpty()){
            question.setError("Can't be Empty");
            return false;
        }
        if(optionA.getText().toString().isEmpty()){
            optionA.setError("Can't be Empty");
            return false;
        }
        if(optionB.getText().toString().isEmpty()){
            optionB.setError("Can't be Empty");
            return false;
        }
        if(optionC.getText().toString().isEmpty()){
            optionC.setError("Can't be Empty");
            return false;
        }
        if(optionD.getText().toString().isEmpty()){
            optionD.setError("Can't be Empty");
            return false;
        }
        if(correctOption.getText().toString().isEmpty()){
            correctOption.setError("Can't be Empty");
            return false;
        }
        return true;
    }

    private void init(){
        reviewTest = findViewById(R.id.addQuesReviewTest);
        testNo = findViewById(R.id.addQuesTestName);
        noOfQuestions = findViewById(R.id.addQuesQuestions);
        maxTime = findViewById(R.id.addQuesEditMaxTime);
        totalScore = findViewById(R.id.addQuesEditMaxScore);
        quesNo = findViewById(R.id.addQuesQuesNo);
        question = findViewById(R.id.addQuesquestion);
        optionA = findViewById(R.id.addQuesoptionA);
        optionB = findViewById(R.id.addQuesoptionB);
        optionC = findViewById(R.id.addQuesoptionC);
        optionD = findViewById(R.id.addQuesoptionD);
        correctOption = findViewById(R.id.addQuescorrect_option);
        addQuestionButton = findViewById(R.id.addQuesAddToTest);
        addNewQuestionButton = findViewById(R.id.addQuesAddNewQues);
        newQuesLayout = findViewById(R.id.addQuesll2);
        newQuesLayout.setVisibility(View.GONE);

        toolbar = findViewById(R.id.addQuesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DB.g_catList.get(DB.g_selectedSubjectIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        testNo.setText(DB.g_testList.get(DB.g_selectTestIndex).getTestId());
        noOfQuestions.setText("Questions : "+ String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getNoOfQuestions()));
        maxTime.setText(String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getTime()));
        totalScore.setText(String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getMaxScore()));
        quesNo.setText("Questions No. : "+ String.valueOf(DB.g_testList.get(DB.g_selectTestIndex).getNoOfQuestions()+1));


    }
}