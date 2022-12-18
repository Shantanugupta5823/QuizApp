package com.example.adminquiz.Adapter;

import android.app.Dialog;
import android.content.Intent;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.adminquiz.AddQuestionsActivity;
import com.example.adminquiz.DataBase.DB;
import com.example.adminquiz.R;
import com.example.adminquiz.Model.TestModel;
import com.example.adminquiz.myCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class TestAdapter extends BaseAdapter {

    private List<TestModel> testList;
    private Dialog dialog;
    private Button yes,no;

    public TestAdapter(List<TestModel> testList) {
        this.testList = testList;
    }

    @Override
    public int getCount() {
        return testList.size();
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item_layout,parent,false);
        }else{
            view = convertView;
        }
        TextView testID = view.findViewById(R.id.testID);
        TextView testTime = view.findViewById(R.id.testTime);

        testID.setText("Test No. "+ String.valueOf(position+1));
        testTime.setText(String.valueOf(testList.get(position).getTime()) + " mins");

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB.g_selectTestIndex = position;
                Intent intent = new Intent(view.getContext(), AddQuestionsActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dialog = new Dialog(view.getContext());
                dialog.setContentView(R.layout.delete_dialog_test);
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
                        DB.g_firestore.collection("Quiz ")
                                .document(DB.g_catList.get(DB.g_selectedSubjectIndex).getDocID())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        long count = documentSnapshot.getLong("No_Of_Tests");
                                        count = count-1;
                                        Map<String,Object> map = new ArrayMap<>();
                                        map.put("No_Of_Tests",count);
                                        DB.g_catList.get(DB.g_selectedSubjectIndex).setNoOfTests((int) count);
                                        DB.g_firestore.collection("Quiz ")
                                                .document(DB.g_catList.get(DB.g_selectedSubjectIndex).getDocID())
                                                .update(map)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

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
                                        Log.d("Hello","Failed on modifying test numbers");
                                    }
                                });

                        Map<String,Object> del = new ArrayMap<>();
                        del.put("Test_"+(position+1)+"_ID", FieldValue.delete());
                        del.put("Test_"+(position+1)+"_MaxScore", FieldValue.delete());
                        del.put("Test_"+(position+1)+"_Time", FieldValue.delete());
                        del.put("Test_"+(position+1)+"_NoOfQuestions", FieldValue.delete());

                          DB.g_firestore.collection("Quiz ")
                                  .document(DB.g_catList.get(DB.g_selectedSubjectIndex).getDocID())
                                  .collection("Test_List").document("Test_Info")
                                  .update(del)
                                  .addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {
                                          testList.remove(position);
                                          notifyDataSetChanged();
                                      }
                                  })
                                  .addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull @NotNull Exception e) {
                                          Toast.makeText(view.getContext(), "Couldn't Delete the Test", Toast.LENGTH_SHORT).show();
                                      }
                                  });

                          dialog.dismiss();
                    }
                });
                return true;
            }
        });

        return view;
    }
}
