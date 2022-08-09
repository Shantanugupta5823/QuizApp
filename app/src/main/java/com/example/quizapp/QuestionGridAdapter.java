package com.example.quizapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import static com.example.quizapp.dbQuery.ANSWERED;
import static com.example.quizapp.dbQuery.NOT_VISITED;
import static com.example.quizapp.dbQuery.REVIEW;
import static com.example.quizapp.dbQuery.UNANSWERED;

public class  QuestionGridAdapter extends BaseAdapter {

    public QuestionGridAdapter(Context context ,int noOfQues) {
        this.noOfQues = noOfQues;
        this.context = context;
    }

    private int noOfQues;
    private Context context;

    @Override
    public int getCount() {
        return noOfQues;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view;
       if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_grid_item,parent,false);

       }else{
            view = convertView;
       }

       TextView quesTV =  view.findViewById(R.id.ques_num);
       quesTV.setText(String.valueOf(position+1));

       switch (dbQuery.g_questionList.get(position).getStatus()){
           case NOT_VISITED:
               quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.grey)));
               break;
           case UNANSWERED:
               quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.red)));
               break;
           case ANSWERED:
               quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.green)));
               break;
           case REVIEW:
               quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(view.getContext(),R.color.pink)));
               break;

           default:
               break;
       }
       view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(context instanceof QuestionActivity){
                   ((QuestionActivity)context).gotoQuestion(position);
               }
           }
       });



        return view;



    }
}
