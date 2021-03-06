package com.example.quizapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private GridView CatView;


    public CategoryFragment(){ }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
        CatView = view.findViewById(R.id.categoryGrid);

        CategoryAdapter adapter = new CategoryAdapter(dbQuery.g_catList);
        CatView.setAdapter(adapter);

        return view;

    }
}