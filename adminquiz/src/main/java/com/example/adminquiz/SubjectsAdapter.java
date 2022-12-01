package com.example.adminquiz;

import android.app.Dialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SubjectsAdapter extends BaseAdapter {

    private List<SubjectModel>  subjectList;
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
                 dialog.setContentView(R.layout.delete_dialog);
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
                          DB.deleteSubject(String.valueOf(subjectList.get(position).getDocID()), new myCompleteListener() {
                              @Override
                              public void onSuccess() {
                                  Intent intent = new Intent(view.getContext(),SubjectsPage.class);
                                  view.getContext().startActivity(intent);
                                  dialog.dismiss();

                              }
                              @Override
                              public void onFailure() {
                                    dialog.dismiss();
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
//                DB.g_selectedSubjectIndex = position;
//                Intent intent = new Intent(view.getContext(),TestActivity.class);
//                view.getContext().startActivity(intent);

            }
        });
        subjectName.setText(subjectList.get(position).getName());
        subjectTests.setText(String.valueOf(subjectList.get(position).getNoOfTests())+" Tests");

        return view;
    }
}
