package com.example.adminquiz.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.media.tv.TvContentRating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.adminquiz.DataBase.DB;
import com.example.adminquiz.Model.QuestionModel;
import com.example.adminquiz.R;
import com.example.adminquiz.ReviewQuestions;
import com.example.adminquiz.myCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
public class ReviewAdapter extends BaseAdapter {
    List<QuestionModel> q_list;
    public ReviewAdapter(List<QuestionModel> q_list) {
        this.q_list = q_list;
    }

    @Override
    public int getCount() {
        return q_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.art_item_layout,parent,false);
        }else{
            view = convertView;
        }


        TextView questionNo = (TextView) view.findViewById(R.id.rta_quesNo);
        TextView ques = (TextView)view.findViewById(R.id.rta_question);
        TextView optionA = (TextView)view.findViewById(R.id.rta_optionA);
        TextView optionB = (TextView)view.findViewById(R.id.rta_optionB);
        TextView optionC = (TextView)view.findViewById(R.id.rta_optionC);
        TextView optionD = (TextView)view.findViewById(R.id.rta_optionD);
        ImageButton deleteBtn = view.findViewById(R.id.reviewPageDeleteButton);
//        ImageButton editBtn = view.findViewById(R.id.reviewPageEditButton);

        questionNo.setText("Question No. "+ (position+1));
        ques.setText(q_list.get(position).getQues());
        optionA.setText("A. "+q_list.get(position).getOptionA());
        optionB.setText("B. "+q_list.get(position).getOptionB());
        optionC.setText("C. "+q_list.get(position).getOptionC());
        optionD.setText("D. "+q_list.get(position).getOptionD());

        if(q_list.get(position).getCorrectAns() == 1){
            optionA.setTextColor(view.getContext().getColor(R.color.green));
        }else if(q_list.get(position).getCorrectAns() == 2){
            optionB.setTextColor(view.getContext().getColor(R.color.green));
        }else if(q_list.get(position).getCorrectAns() == 3){
            optionC.setTextColor(view.getContext().getColor(R.color.green));
        }else if(q_list.get(position).getCorrectAns() == 4){
            optionD.setTextColor(view.getContext().getColor(R.color.green));
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.g_firestore.collection("Questions").document(q_list.get(position).getDocID())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                 q_list.remove(position);
                                 notifyDataSetChanged();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Toast.makeText(view.getContext(), "Sorry,Couldn't delete Question", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

        });

//        editBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ReviewQuestions r1 = new ReviewQuestions();
//                r1.setDailog(q_list,position);
//            }
//        });
        return view;
    }

}
