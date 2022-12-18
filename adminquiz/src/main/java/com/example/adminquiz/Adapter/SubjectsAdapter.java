package com.example.adminquiz.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.adminquiz.DataBase.DB;
import com.example.adminquiz.R;
import com.example.adminquiz.Model.SubjectModel;
import com.example.adminquiz.TestActivity;
import com.example.adminquiz.myCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubjectsAdapter extends BaseAdapter {

    private List<SubjectModel> subjectList;
    private Dialog dialog;
    private Button yes,no;
    public SubjectsAdapter(List<SubjectModel> subjectList){
        this.subjectList = subjectList;
    }
    @Override
    public int getCount() {
        return subjectList.size();
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
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item_layout,parent,false);
       }else{
           view = convertView;
       }
        TextView subjectName = view.findViewById(R.id.subjectName);
        TextView subjectTests = view.findViewById(R.id.subjectTest);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                 dialog = new Dialog(view.getContext());
                 dialog.setContentView(R.layout.delete_dialog_subject);
                 dialog.setCancelable(false);
                 dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                 yes = dialog.findViewById(R.id.yesButton);
                 no = dialog.findViewById(R.id.noButton);

                 dialog.show();
                 no.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         dialog.dismiss();
                     }
                 });
                  yes.setOnClickListener(new View.OnClickListener() {
                      @Override
                      public void onClick(View v) {
                          String docID = subjectList.get(position).getDocID();
                          DB.g_firestore.collection("Quiz ").document(docID)
                                  .delete()
                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {
                                           Log.d("Hello","Subject Deleted");
                                      }
                                  })
                                  .addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull @NotNull Exception e) {
                                          Log.d("Hello","Subject Couldn't be deleted");
                                      }
                                  });

//      Deleting data from Categories
                          DB.g_firestore.collection("Quiz ").document("Categories")
                                  .get()
                                  .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                      @Override
                                      public void onSuccess(DocumentSnapshot documentSnapshot) {
                                          long ans = 0;
                                          long count = documentSnapshot.getLong("Count");
                                          for(int i=1; i<=count; i++){
                                              if(docID.equals(documentSnapshot.getString("Cat"+i+"_ID"))){
                                                  ans = i;
                                                  break;
                                              }
                                          }
                                          count = count-1;
                                          Map<String,Object> del = new HashMap<>();
                                          del.put(("Cat"+ans+"_ID"), FieldValue.delete());
                                          del.put("Count",count);

                                          DB.g_firestore.collection("Quiz ").document("Categories").update(del)
                                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                      @Override
                                                      public void onSuccess(Void unused) {
                                                          Log.d("Hello","changes done ");
                                                      }
                                                  })
                                                  .addOnFailureListener(new OnFailureListener() {
                                                      @Override
                                                      public void onFailure(@NonNull @NotNull Exception e) {

                                                      }
                                                  });
                                      }
                                  })
                                  .addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull @NotNull Exception e) {

                                      }
                                  });

                      }
                  });
                return true;
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.g_selectedSubjectIndex = position;
                Intent intent = new Intent(view.getContext(), TestActivity.class);
                view.getContext().startActivity(intent);

            }
        });
        subjectName.setText(subjectList.get(position).getName());
        subjectTests.setText(String.valueOf(subjectList.get(position).getNoOfTests())+" Tests");

        return view;
    }


}
