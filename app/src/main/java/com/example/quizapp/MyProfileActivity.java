package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.DataBase.dbQuery;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name, email, phoneNo;
//    private LinearLayout editB;
    private Button cancelB, saveB;
    private ImageButton editB;
    private TextView profileText,dialogtext;
    private ImageView profileImg;
    private LinearLayout buttonLayout;
    private String nameStr , phoneNoStr;
    private Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogtext = progressDialog.findViewById(R.id.dialog_text);
        dialogtext.setText("Updating Data...");

        init();

        disableEdit();

        {Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");}

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEdit();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEdit();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    saveData();
                }
            }
        });
    }
    private void saveData() {
        progressDialog.show();

        dbQuery.saveProfileData(nameStr, phoneNoStr, new myCompleteListner() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this,"Profile Updated Successfully",Toast.LENGTH_SHORT).show();
                disableEdit();
                progressDialog.dismiss();
            }
            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this,"Something Went Wrong",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private boolean validate() {
        nameStr = name.getText().toString();
        phoneNoStr = phoneNo.getText().toString();

        if(nameStr.isEmpty()){
            name.setError("Name Cannot be Empty");
            return false;
        }
        if(! (phoneNoStr.isEmpty())){
            if(phoneNoStr.length() != 10){
                phoneNo.setError("Wrong Length");
                return false;
            }
            if(!(TextUtils.isDigitsOnly(phoneNoStr))){
                phoneNo.setError("Contains Non-digits");
                return  false;
            }
        }
        return true;
    }

    private void disableEdit() {
        name.setEnabled(false);
        email.setEnabled(false);
        phoneNo.setEnabled(false);
        buttonLayout.setVisibility(View.GONE);

        name.setText(dbQuery.myProfile.getName());
        email.setText(dbQuery.myProfile.getEmail());
        phoneNo.setText(dbQuery.myProfile.getPhoneNo());

        profileText.setText(dbQuery.myProfile.getName().toUpperCase().substring(0,1));
        profileText.setEnabled(false);
    }
    private void enableEdit(){

        name.setEnabled(true);
        phoneNo.setEnabled(true);
        buttonLayout.setVisibility(View.VISIBLE);

    }
    private void init(){
        name = findViewById(R.id.my_profile_name);
        email = findViewById(R.id.my_profile_email);
        phoneNo = findViewById(R.id.my_profile_phoneNo);
        editB = findViewById(R.id.my_profile_edit);
        cancelB = findViewById(R.id.button_cancel);
        saveB = findViewById(R.id.buttton_save);
        profileText = findViewById(R.id.my_profile_img_text);
        profileImg = findViewById(R.id.my_profile_img);
        buttonLayout = findViewById(R.id.my_profile_button_layout);
    }
}