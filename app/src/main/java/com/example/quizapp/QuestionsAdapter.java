package com.example.quizapp;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.example.quizapp.dbQuery.ANSWERED;
import static com.example.quizapp.dbQuery.REVIEW;
import static com.example.quizapp.dbQuery.UNANSWERED;
import static com.example.quizapp.dbQuery.g_questionList;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.viewHolder> {

    private List<QuestionModel> questionList;

    public QuestionsAdapter(List<QuestionModel> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @NotNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull viewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        private TextView question;
        private Button optionA, optionB, optionC, optionD, prevSelectedBtn;
        public viewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            question =  itemView.findViewById(R.id.questions);
            optionA = itemView.findViewById(R.id.optionA);
            optionB = itemView.findViewById(R.id.optionB);
            optionC = itemView.findViewById(R.id.optionC);
            optionD = itemView.findViewById(R.id.optionD);
            prevSelectedBtn = null;
        }
        private void setData(final int position ){
            question.setText(questionList.get(position).getQuestion());
            optionA.setText(questionList.get(position).getOptionA());
            optionB.setText(questionList.get(position).getOptionB());
            optionC.setText(questionList.get(position).getOptionC());
            optionD.setText(questionList.get(position).getOptionD());

            setOption(optionA,1,position);
            setOption(optionB,2,position);
            setOption(optionC,3,position);
            setOption(optionD,4,position);

            optionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        selectOption(optionA, 1, position);
                }
            });
            optionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        selectOption(optionB,2,position);
                }
            });
            optionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionC , 3, position);
                }
            });
            optionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectOption(optionD,4,position);
                }
            });




        }
        private void selectOption(Button button , int optionNo , int quesID){
            if(prevSelectedBtn == null){
                    button.setBackgroundResource(R.drawable.selected_btn);
                    dbQuery.g_questionList.get(quesID).setSelectedAns(optionNo);
                    changeStatus(quesID,ANSWERED);
                    prevSelectedBtn = button;
            }else{
                    if(prevSelectedBtn.getId() == button.getId()){
                        button.setBackgroundResource(R.drawable.unselected_btn);
                        dbQuery.g_questionList.get(quesID).setSelectedAns(-1);
                        changeStatus(quesID,UNANSWERED);
                        prevSelectedBtn = null;
                    }else{
                        prevSelectedBtn.setBackgroundResource(R.drawable.unselected_btn);
                        button.setBackgroundResource(R.drawable.selected_btn);
                        dbQuery.g_questionList.get(quesID).setSelectedAns(optionNo);
                        changeStatus(quesID,ANSWERED);
                        prevSelectedBtn = button;
                    }
            }
        }
        private void changeStatus(int id , int status){
            if( g_questionList.get(id).getStatus() != REVIEW){
                g_questionList.get(id).setStatus(status);
            }
        }
        private void setOption(Button button , int optionNo, int quesID){

            if(dbQuery.g_questionList.get(quesID).getSelectedAns() == optionNo){
                button.setBackgroundResource(R.drawable.selected_btn);
            }else{
                button.setBackgroundResource(R.drawable.unselected_btn);
            }

        }
    }
}
