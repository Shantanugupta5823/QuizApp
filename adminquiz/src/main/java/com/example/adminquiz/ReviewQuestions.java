package com.example.adminquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminquiz.Adapter.ReviewAdapter;
import com.example.adminquiz.DataBase.DB;
import com.example.adminquiz.Model.QuestionModel;

import java.util.List;

public class ReviewQuestions extends AppCompatActivity {
    private GridView grid;
//    private EditText editQuesNo,editQuestion,editOptionA,editOptionB,editOptionC,editOptionD,editOptionCorrect;
//    private Button editSave,editCancel;
//    private Dialog editDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_questions);

        grid = findViewById(R.id.questionGrid);
        Toolbar toolbar = findViewById(R.id.reviewPageToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Review Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        editDialog = new Dialog(ReviewQuestions.this);
//        editDialog.setContentView(R.layout.dailog_edit_questions);
//        editDialog.setCancelable(false);
//        editDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        editQuesNo = editDialog.findViewById(R.id.edit_test_quesNo);
//        editQuestion = editDialog.findViewById(R.id.edit_test_question);
//        editOptionA = editDialog.findViewById(R.id.edit_test_optionA);
//        editOptionB = editDialog.findViewById(R.id.edit_test_optionB);
//        editOptionC = editDialog.findViewById(R.id.edit_test_optionC);
//        editOptionD = editDialog.findViewById(R.id.edit_test_optionD);
//        editOptionCorrect = editDialog.findViewById(R.id.edit_test_correct_option);
//        editSave = editDialog.findViewById(R.id.edit_test_save);
//        editCancel = editDialog.findViewById(R.id.edit_test_cancel);


        ReviewAdapter adapter = new ReviewAdapter(DB.g_questionList);
        grid.setAdapter(adapter);

    }
//    public void setDailog(List<QuestionModel> q_list,int position){
//        editQuesNo.setText("Question No. "+ (position+1));
//        editQuestion.setText(q_list.get(position).getQues());
//        editOptionA.setText(q_list.get(position).getOptionA());
//        editOptionB.setText(q_list.get(position).getOptionB());
//        editOptionC.setText(q_list.get(position).getOptionC());
//        editOptionD.setText(q_list.get(position).getOptionD());
//        editOptionCorrect.setText(q_list.get(position).getCorrectAns());
//        editDialog.show();
//        editCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                editDialog.dismiss();
//            }
//        });
//        editSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(validate()){
//                    DB.refactorQuestion(q_list.get(position).getDocID(),
//                            editQuestion.getText().toString(),
//                            editOptionA.getText().toString(),
//                            editOptionB.getText().toString(),
//                            editOptionC.getText().toString(),
//                            editOptionD.getText().toString(),
//                            Integer.valueOf(editOptionCorrect.getText().toString()),
//                            new myCompleteListener() {
//                                @Override
//                                public void onSuccess() {
//                                    Toast.makeText(ReviewQuestions.this, "Question Edited successfully", Toast.LENGTH_SHORT).show();
//                                    editDialog.dismiss();
//                                }
//
//                                @Override
//                                public void onFailure() {
//                                    Toast.makeText(ReviewQuestions.this, "Sorry,Couldn't edit question", Toast.LENGTH_SHORT).show();
//                                    editDialog.dismiss();
//                                }
//                            });
//                }
//            }
//            private boolean validate() {
//                if(editQuestion.getText().toString().isEmpty()){
//                    editQuestion.setError("Can't be left empty");
//                    return false;
//                }
//                if(editOptionA.getText().toString().isEmpty()){
//                    editOptionA.setError("Can't be left empty");
//                    return false;
//                }
//                if(editOptionB.getText().toString().isEmpty()){
//                    editOptionB.setError("Can't be left empty");
//                    return false;
//                }
//                if(editOptionC.getText().toString().isEmpty()){
//                    editOptionC.setError("Can't be left empty");
//                    return false;
//                }
//                if(editOptionD.getText().toString().isEmpty()){
//                    editOptionD.setError("Can't be left empty");
//                    return false;
//                }
//                if(editOptionCorrect.getText().toString().isEmpty()){
//                    editOptionCorrect.setError("Can't be left empty");
//                    return false;
//                }
//                return true;
//            }
//        });
//    }
}