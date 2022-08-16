package com.example.quizapp.Adapter;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizapp.R;
import com.example.quizapp.model.QuestionModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder> {

    private List<QuestionModel> quesList;

    public BookMarkAdapter(List<QuestionModel> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answers_item_layout,parent,false);
        return new  BookMarkAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        String question = quesList.get(position).getQuestion();
        String optionA = quesList.get(position).getOptionA();
        String optionB = quesList.get(position).getOptionB();
        String optionC = quesList.get(position).getOptionC();
        String optionD = quesList.get(position).getOptionD();
        int result = quesList.get(position).getCorrectAns();

        holder.setData(position,question,optionA,optionB,optionC,optionD , result);

    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView quesNo, question, optionA, optionB, optionC, optionD, optionResult;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            quesNo = itemView.findViewById(R.id.ans_quesNo);
            question = itemView.findViewById(R.id.ans_question);
            optionA =itemView.findViewById(R.id.ans_optionA);
            optionB = itemView.findViewById(R.id.ans_optionB);
            optionC =itemView.findViewById(R.id.ans_optionC);
            optionD = itemView.findViewById(R.id.ans_optionD);
            optionResult = itemView.findViewById(R.id.ans_optionCorrect);
        }

        private void setData(int pos, String ques, String A, String B, String C,String D , int correctAns){

            quesNo.setText("Question No : "+String.valueOf(pos+1));
            question.setText(ques);
            optionA.setText("A. " +A);
            optionB.setText("B. " +B);
            optionC.setText("C. " +C);
            optionD.setText("D. " +D);

            if(correctAns == 1){
                optionResult.setText("Correct : " + A);
                optionResult.setTypeface(null, Typeface.BOLD);
                optionA.setTextColor(itemView.getContext().getColor(R.color.green));
            }else if(correctAns == 2){
                optionResult.setText("Correct : " + B);
                optionResult.setTypeface(null, Typeface.BOLD);
                optionB.setTextColor(itemView.getContext().getColor(R.color.green));
            }else if(correctAns == 3){
                optionResult.setText("Correct : " + C);
                optionResult.setTypeface(null, Typeface.BOLD);
                optionC.setTextColor(itemView.getContext().getColor(R.color.green));
            }else{
                optionResult.setText("Correct : " + D);
                optionResult.setTypeface(null, Typeface.BOLD);
                optionD.setTextColor(itemView.getContext().getColor(R.color.green));
            }
        }
    }
}


