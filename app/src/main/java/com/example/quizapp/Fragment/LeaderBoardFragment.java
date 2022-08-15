package com.example.quizapp.Fragment;

import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapter.RankAdapter;
import com.example.quizapp.DataBase.dbQuery;
import com.example.quizapp.MainActivity;
import com.example.quizapp.R;
import com.example.quizapp.TestActivity;
import com.example.quizapp.myCompleteListner;

public class LeaderBoardFragment extends Fragment {
    private TextView totalUsersTV , myImgTextTV, myNameTV, myScoreTV, myRankTV, dialogtext;
    private RecyclerView usersView;
    private RankAdapter adapter;
    private Dialog progressDialog;

    public LeaderBoardFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_leader_board, container, false);

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Leader Board");

        initViews(view);

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        usersView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(dbQuery.g_usersList);
        usersView.setAdapter(adapter);

        dbQuery.getTopUsers(new myCompleteListner() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if(dbQuery.myPerformance.getScore() != 0){
                    if(!dbQuery.isMeOnTop){
                        calculateRank();
                    }
                    myNameTV.setText(dbQuery.myPerformance.getName());
                    myScoreTV.setText("Score : "+ dbQuery.myPerformance.getScore());
                    if(dbQuery.myPerformance.getScore() != 0){
                        myRankTV.setText("Rank : " + dbQuery.myPerformance.getRank());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {

                progressDialog.dismiss();
                Toast.makeText(getContext(),"Something went Wrong",Toast.LENGTH_SHORT).show();

            }
        });

        totalUsersTV.setText(new StringBuilder().append("Total Users : ").append(dbQuery.g_usersCount).toString());
        myImgTextTV.setText(dbQuery.myPerformance.getName().toUpperCase().substring(0,1));

          return view;
    }

    private void calculateRank() {

        int lowTopScore = dbQuery.g_usersList.get(dbQuery.g_usersList.size() - 1).getScore();
        int remainingSlots = dbQuery.g_usersCount-20;

        int mySlot = (dbQuery.myPerformance.getScore() * remainingSlots)/lowTopScore;

        int rank;

        if(lowTopScore != dbQuery.myPerformance.getScore()){
            rank = dbQuery.g_usersCount - mySlot;
        }else{
            rank = 21;
        }
        dbQuery.myPerformance.setRank(rank);
    }

    private void initViews(View view) {
        totalUsersTV = view.findViewById(R.id.ld_total_users);
        myImgTextTV = view.findViewById(R.id.ld_img_text);
        myNameTV = view.findViewById(R.id.ld_name);
        myScoreTV = view.findViewById(R.id.ld_score);
        myRankTV = view.findViewById(R.id.ld_rank);
        usersView = view.findViewById(R.id.ld_users_view);


    }
}