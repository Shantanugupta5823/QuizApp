 package com.example.quizapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

 public class splashActivity extends AppCompatActivity {
    private TextView appName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        appName = findViewById(R.id.appName);
        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist);
        appName.setTypeface(typeface);

        Animation anim  = AnimationUtils.loadAnimation(this,R.anim.splashanim);
        appName.setAnimation(anim);
        mAuth = FirebaseAuth.getInstance();

        dbQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread(){
            @Override
            public void run() {
                try{
                    sleep(3000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(mAuth.getCurrentUser() != null){
                   dbQuery.loadData(new myCompleteListner() {
                       @Override
                       public void onSuccess() {
                           Intent intent = new Intent(splashActivity.this,MainActivity.class);
                           startActivity(intent);
                           finish();
                       }

                       @Override
                       public void onFailure() {
                           Toast.makeText(splashActivity.this,"Something Went Wrong, Please try Again Later.",
                                   Toast.LENGTH_SHORT).show();
                       }
                   });
                }else{
                    Intent intent = new Intent(splashActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }


            }
        }.start();
    }
}