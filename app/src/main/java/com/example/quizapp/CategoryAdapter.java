package com.example.quizapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private List<categoryModel> catList;

    public CategoryAdapter(List<categoryModel> catList) {
        this.catList = catList;
    }

    @Override
    public int getCount() {
        return catList.size();
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item_layout,parent,false);
        }else{
            view = convertView;
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbQuery.g_selectedCatIndex = position;
                Intent intent = new Intent(view.getContext(),TestActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        TextView catName = view.findViewById(R.id.catName);
        TextView catTests = view.findViewById(R.id.catTest);

        catName.setText(catList.get(position).getName());
        catTests.setText(String.valueOf(catList.get(position).getNoOfTest()) + " Tests");
        return view;
    }
}
