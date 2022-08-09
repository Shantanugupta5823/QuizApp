package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import static com.example.quizapp.dbQuery.ANSWERED;
import static com.example.quizapp.dbQuery.NOT_VISITED;
import static com.example.quizapp.dbQuery.REVIEW;
import static com.example.quizapp.dbQuery.UNANSWERED;
import static com.example.quizapp.dbQuery.g_questionList;

public class QuestionActivity  extends AppCompatActivity {

    private RecyclerView questionView;
    private TextView quesID_tv, timer_tv , catName_tv;
    private Button submitButton , markForReviewButton , clearSelectionButton;
    private ImageView bookmark , queGrid , markImg;
    private ImageButton prev_button , next_button , drawerCloseButton;
    private int QuesID;
    QuestionsAdapter quesAdapter;
    private DrawerLayout drawer;
    private GridView ques_List_gv;
    private QuestionGridAdapter gridAdapter;
    private CountDownTimer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_list_layout);

        init();

        quesAdapter = new QuestionsAdapter(g_questionList);
        questionView.setAdapter(quesAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        questionView.setLayoutManager(layoutManager);

        gridAdapter = new QuestionGridAdapter(this,g_questionList.size());
        ques_List_gv.setAdapter(gridAdapter);

        setSnapHelper();
        setClickListner();
        startTimer();

    }

    private void init(){
        questionView = findViewById(R.id.quesView);
        quesID_tv = findViewById(R.id.topBar_quesNo);
        timer_tv = findViewById(R.id.topBar_timer);
        catName_tv = findViewById(R.id.middleBar_SubjectName);
        submitButton = findViewById(R.id.topBar_submitButton);
        markForReviewButton = findViewById(R.id.bottomBar_markForReview);
        clearSelectionButton = findViewById(R.id.bottomBar_clearSelection);
        bookmark = findViewById(R.id.middleBar_bookmark);
        queGrid = findViewById(R.id.middleBar_quesLayout);
        prev_button = findViewById(R.id.bottomBar_prevQue);
        next_button = findViewById(R.id.bottomBar_nextQue);
        drawer = findViewById(R.id.drawer_layout);
        drawerCloseButton = findViewById(R.id.drawer_close_btn);
        markImg = findViewById(R.id.mark_image);
        ques_List_gv = findViewById(R.id.ques_list_gv);
        QuesID = 0;

        quesID_tv.setText("1/"+String.valueOf(dbQuery.g_questionList.size()));
        catName_tv.setText(dbQuery.g_catList.get(dbQuery.g_selectedCatIndex).getName());

        g_questionList.get(0).setStatus(UNANSWERED);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(QuestionActivity.this,MainActivity.class);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        });

    }
    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(questionView);

        questionView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull @NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                QuesID = recyclerView.getLayoutManager().getPosition(view);

                if(dbQuery.g_questionList.get(QuesID).getStatus() == NOT_VISITED){
                    g_questionList.get(QuesID).setStatus(UNANSWERED);
                }
                if(g_questionList.get(QuesID).getStatus() == REVIEW){
                     markImg.setVisibility(View.VISIBLE);
                }else{
                    markImg.setVisibility(View.GONE);
                }
                quesID_tv.setText(String.valueOf(QuesID+1)+"/"+String.valueOf(dbQuery.g_questionList.size()));
            }

            @Override
            public void onScrolled(@NonNull @NotNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }
    private void setClickListner(){
        prev_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuesID>0){
                    questionView.smoothScrollToPosition(QuesID-1);
                }
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(QuesID < dbQuery.g_questionList.size()){
                    questionView.smoothScrollToPosition(QuesID+1);
                }
            }
        });

        clearSelectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbQuery.g_questionList.get(QuesID).setSelectedAns(-1);
                g_questionList.get(QuesID).setStatus(UNANSWERED);
                markImg.setVisibility(View.GONE);
                quesAdapter.notifyDataSetChanged();
            }
        });

        queGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawer.isDrawerOpen(GravityCompat.END)){
                    gridAdapter.notifyDataSetChanged();
                    drawer.openDrawer(GravityCompat.END);
                }
            }
        });

        drawerCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.isDrawerOpen(GravityCompat.END)){
                    drawer.closeDrawer(GravityCompat.END);
                }
            }
        });

        markForReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(markImg.getVisibility() != View.VISIBLE){
                    markImg.setVisibility(View.VISIBLE);
                    g_questionList.get(QuesID).setStatus(REVIEW);
                }else{
                    markImg.setVisibility(View.GONE);
                    if(g_questionList.get(QuesID).getSelectedAns() != -1){
                        g_questionList.get(QuesID).setStatus(ANSWERED);
                    }else{
                        g_questionList.get(QuesID).setStatus(UNANSWERED);
                    }
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitTest();
            }
        });
    }

    private void submitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
        builder.setCancelable(true);

        View view = getLayoutInflater().inflate(R.layout.alert_dialog_layout,null);
        Button submit = view.findViewById(R.id.submit_button);
        Button cancel = view.findViewById(R.id.cancel_button);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                alertDialog.dismiss();
                Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
                startActivity(intent);
                QuestionActivity.this.finish();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
    private void startTimer() {
        long totaltime = dbQuery.g_testList.get(dbQuery.g_SelectedTestIndex).getTime()*60*1000;
        Log.d("TimerTime", String.valueOf(totaltime));
        timer = new CountDownTimer(totaltime + 1000,1000) {
            @Override
            public void onTick(long remainingTime) {

                    String time = String.format("%02d:%02d min"
                            , TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                            TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));
                    timer_tv.setText(time);

            }
            @Override
            public void onFinish() {
                Intent intent = new Intent(QuestionActivity.this,ScoreActivity.class);
                startActivity(intent);
                QuestionActivity.this.finish();
            }
        };
        timer.start();

    }
    public void gotoQuestion(int position){
        questionView.smoothScrollToPosition(position);

        if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }
    }
}