//package com.example.adminquiz;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//import android.util.ArrayMap;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.Map;
//
//public class AddQuestion extends AppCompatActivity {
//    private RadioGroup subject;
//    private RadioButton radioButton;
//    private String Cat_ID;
//    private Button submit;
//    private static FirebaseFirestore g_fireStore;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_question);
//
//      EditText question = findViewById(R.id.question);
//        EditText option1 = findViewById(R.id.option1);
//        EditText option2 = findViewById(R.id.option2);
//        EditText option3 = findViewById(R.id.option3);
//        EditText option4 = findViewById(R.id.option4);
//        EditText correctOption = findViewById(R.id.correctOption);
//        EditText test_id = findViewById(R.id.Test_id);
//        subject = findViewById(R.id.radioGrp);
//        submit = findViewById(R.id.submitbutton);
//
//        String Ques = question.getText().toString();
//        String opt1 = option1.getText().toString();
//        String opt2 = option2.getText().toString();
//        String opt3 = option3.getText().toString();
//        String opt4 = option4.getText().toString();
//        String correctopt = correctOption.getText().toString();
//        String test_ID = test_id.getText().toString();
//
//        Toast.makeText(AddQuestion.this, Ques+" "+opt1+" ",Toast.LENGTH_SHORT).show();
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(Ques.isEmpty() && opt1.isEmpty() && opt2.isEmpty() && opt3.isEmpty() && opt4.isEmpty() && correctopt.isEmpty() && test_ID.isEmpty()){
//                    Toast.makeText(AddQuestion.this, "things are empty out here", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                int select = subject.getCheckedRadioButtonId();
//                radioButton = findViewById(select);
//                String name = radioButton.getText().toString();
//                if(name.equals("Hindi")){
//                    Cat_ID = "g2NHyZR7i8BkynUwnDJS";
//                }else if(name.equals("Maths")){
//                    Cat_ID = "FQZkoxUWHMEapaBFoCFh";
//                }else if(name.equals("English")){
//                    Cat_ID = "NqRbHdRYiKX2LICTY4Z8";
//                }else{
//                    Toast.makeText(AddQuestion.this,"Select One",Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                Map<String,Object> questionData = new ArrayMap<>();
//
//                questionData.put("Category_ID",Cat_ID);
//                questionData.put("Test_ID",test_ID);
//                questionData.put("Question",Ques);
//                questionData.put("A",opt1);
//                questionData.put("B",opt2);
//                questionData.put("C",opt3);
//                questionData.put("D",opt4);
//                questionData.put("Answer",correctopt);
//
//                g_fireStore = FirebaseFirestore.getInstance();
//                g_fireStore.collection("Questions")
//                        .add(questionData)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Toast.makeText(AddQuestion.this,"Successfully Added",Toast.LENGTH_SHORT).show();
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull @NotNull Exception e) {
//                                Toast.makeText(AddQuestion.this,"Sorry",Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });
//
//
//
//
//
//
//    }
//
//
//}