package com.example.quizapp.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.BookMarkActivity;
import com.example.quizapp.DataBase.dbQuery;
import com.example.quizapp.LoginActivity;
import com.example.quizapp.MainActivity;
import com.example.quizapp.MyProfileActivity;
import com.example.quizapp.R;
import com.example.quizapp.myCompleteListner;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class AccountFragment extends Fragment {

    private LinearLayout logoutButton,leaderB,profile,bookmarksB;
    private TextView profile_img_text , name, rank, score,dialogtext;
    private BottomNavigationView bottomNavigationView;
    private Dialog progressDialog;

    public AccountFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initViews(view);

        progressDialog = new Dialog(getContext());
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Loading...");

        androidx.appcompat.widget.Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("My Account");

        if(dbQuery.g_usersList.size() == 0){
            progressDialog.show();
            dbQuery.getTopUsers(new myCompleteListner() {
                @Override
                public void onSuccess() {
                    if(dbQuery.myPerformance.getScore() != 0){
                        if(!dbQuery.isMeOnTop){
                             calculateRank();
                        }
                        score.setText(String.valueOf(dbQuery.myPerformance.getScore()));
                        name.setText(dbQuery.myPerformance.getName());
                        rank.setText(String.valueOf(dbQuery.myPerformance.getRank()));
                        profile_img_text.setText(dbQuery.myPerformance.getName().toUpperCase().substring(0,1));
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure() {

                    progressDialog.dismiss();
                    Toast.makeText(getContext(),"Something went Wrong",Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            score.setText(String.valueOf(dbQuery.myPerformance.getScore()));
            if(dbQuery.myPerformance.getScore() != 0){
                rank.setText(String.valueOf(dbQuery.myPerformance.getRank()));
            }
            name.setText(dbQuery.myPerformance.getName());
            profile_img_text.setText(dbQuery.myPerformance.getName().toUpperCase().substring(0,1));
        }

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
//
//                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                        .requestIdToken(getString(R.string.default_web_client_id))
//                        .requestEmail()
//                        .build();

//                GoogleSignInClient mgoogleCilent  = GoogleSignIn.getClient(getContext(),gso);
//                mgoogleCilent.signOut().addOnCompleteListener({
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        getActivity().finish();
//                });

            }
        });
        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomNavigationView.setSelectedItemId(R.id.nav_leaderboard);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                startActivity(intent);
            }
        });
        bookmarksB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BookMarkActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void initViews(View view) {
        logoutButton = view.findViewById(R.id.logoutB);
        leaderB = view.findViewById(R.id.leaderboardB);
        profile = view.findViewById(R.id.profileB);
        bookmarksB = view.findViewById(R.id.bookmarked);
        profile_img_text = view.findViewById(R.id.profile_img_text);
        name = view.findViewById(R.id.profile_name);
        rank = view.findViewById(R.id.rank);
        score = view.findViewById(R.id.total_score);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_nav_bar);
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
}