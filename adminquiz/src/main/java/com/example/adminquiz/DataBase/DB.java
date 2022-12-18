package com.example.adminquiz.DataBase;

import android.util.ArrayMap;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.adminquiz.Model.QuestionModel;
import com.example.adminquiz.Model.SubjectModel;
import com.example.adminquiz.Model.TestModel;
import com.example.adminquiz.myCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DB {
    public static List<SubjectModel> g_catList = new ArrayList();
    public static FirebaseFirestore g_firestore;
    public static List<TestModel> g_testList = new ArrayList<>();
    public static List<QuestionModel>g_questionList = new ArrayList<>();
    public static int g_selectedSubjectIndex = 0;
    public static int g_selectTestIndex = 0;

//   Subject Things
    public static void loadCategories(final myCompleteListener completeListner){
        g_firestore = FirebaseFirestore.getInstance();
        g_catList.clear();
        g_firestore.collection("Quiz ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> doclist = new ArrayMap<>();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            doclist.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot catListDoc = doclist.get("Categories");
                        long catCount = catListDoc.getLong("Count");
                        for(int i = 1; i<= catCount; i++){
                            String catID = catListDoc.getString("Cat"+String.valueOf(i)+"_ID");
                            QueryDocumentSnapshot catDoc = doclist.get(catID);
                            int noOfTests = catDoc.getLong("No_Of_Tests").intValue();
                            Log.d("NoOfTests",String.valueOf(noOfTests));
                            String categoryName  = catDoc.getString("Name ");
                            g_catList.add(new SubjectModel(catID,categoryName,noOfTests));
                            Log.d("Print", g_catList.toString());
                        }
                        completeListner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListner.onFailure();
                    }
                });
    }
    public static void addNewSubject(String newSubName , final myCompleteListener completeListener){

        DocumentReference ref = g_firestore.collection("Quiz").document();
        String myId = ref.getId();
        Map<String,Object> subject = new HashMap<>();
        subject.put("Cat_ID",myId);
        subject.put("Name ",newSubName);
        subject.put("No_Of_Tests",0);

        g_firestore.collection("Quiz ").document(myId).set(subject)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        g_firestore.collection("Quiz ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> doclist = new ArrayMap<>();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            doclist.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot catListDoc = doclist.get("Categories");
                        long catCount = catListDoc.getLong("Count");
                        Map<String, Object> addvalues = new ArrayMap<>();
                        catCount++;
                        addvalues.put("Count",catCount);
                        addvalues.put("Cat"+catCount+"_ID",myId);
                        g_firestore.collection("Quiz ").document("Categories").update(addvalues);
                        g_catList.add(new SubjectModel(myId,newSubName,0));
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }


//    Test Things
    public static void loadTestData(final myCompleteListener completeListener){
        g_testList.clear();
        g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                .collection("Test_List").document("Test_Info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selectedSubjectIndex).getNoOfTests();
                        for(int i = 1; i<= noOfTests; i++){
                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("Test_"+String.valueOf(i)+"_ID"),
                                    documentSnapshot.getLong("Test_"+String.valueOf(i)+"_MaxScore").intValue(),
                                    documentSnapshot.getLong("Test_"+String.valueOf(i)+"_Time").intValue(),
                                    documentSnapshot.getLong("Test_"+String.valueOf(i)+"_NoOfQuestions").intValue()
                            ));

                        }
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }
    public static void addNewTest(int time,int score, final myCompleteListener completeListener){

        int noOfTest = g_catList.get(g_selectedSubjectIndex).getNoOfTests();
        Map<String,Object> newTest = new ArrayMap<>();
        String testNo = "Test_"+(noOfTest+1);
        newTest.put(testNo+"_ID","Test_"+(noOfTest+1));
        newTest.put(testNo+"_Time",time);
        newTest.put(testNo+"_MaxScore",score);
        newTest.put(testNo+"_NoOfQuestions",0);

        if(noOfTest!=0){
            g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                    .collection("Test_List").document("Test_Info")
                    .update(newTest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            g_catList.get(g_selectedSubjectIndex).setNoOfTests(noOfTest+1);
                            g_testList.add(new TestModel(("Test_"+(noOfTest+1)),score,time,0));
                            completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            completeListener.onFailure();
                        }
                    });
        }
        else{
            g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                    .collection("Test_List").document("Test_Info")
                    .set(newTest)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            g_catList.get(g_selectedSubjectIndex).setNoOfTests(noOfTest+1);
                            g_testList.add(new TestModel(("Test_"+(noOfTest+1)),score,time,0));
                            completeListener.onSuccess();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            completeListener.onFailure();
                        }
                    });
        }
        g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                .update("No_Of_Tests",(noOfTest+1));



    }
    public static void refactorQuestion(String docID,String question,String A, String B, String C, String D, int ans,final myCompleteListener completeListener){
        Map<String,Object> map  = new ArrayMap<>();
        map.put("Question",question);
        map.put("A",A);
        map.put("B",B);
        map.put("C",C);
        map.put("D",D);
        map.put("Answer",ans);

        g_firestore.collection("Questions").document(docID)
                .update(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });
    }

//    Adding new Question
    public static void addNewQuestion(String ques,String optionA,String optionB,String optionC, String optionD,long correctOption, final myCompleteListener completeListener){

        Map<String,Object> map = new ArrayMap<>();
        map.put("Question",ques);
        map.put("A",optionA);
        map.put("B",optionB);
        map.put("C",optionC);
        map.put("D",optionD);
        map.put("Answer",correctOption);
        map.put("Category_ID",g_catList.get(g_selectedSubjectIndex).getDocID());
        map.put("Test_ID","Test_"+(g_catList.get(g_selectedSubjectIndex).getNoOfTests()+1));

        g_firestore.collection("Questions").add(map)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });

        final long[] noOfQues = {0};
        g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                .collection("Test_List").document("Test_Info")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot doc = task.getResult();
                            noOfQues[0] = doc.getLong(g_testList.get(g_selectTestIndex).getTestId()+"_NoOfQuestions");
                        }
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {

                    }
                });
        noOfQues[0]++;
        long finalNoOfQues = noOfQues[0];
        g_firestore.collection("Quiz ").document(g_catList.get(g_selectedSubjectIndex).getDocID())
                .collection("Test_List").document("Test_Info")
                .update(g_testList.get(g_selectTestIndex).getTestId()+"_NoOfQuestions",Long.valueOf(noOfQues[0]))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        g_testList.get(g_selectTestIndex).setNoOfQuestions((int) finalNoOfQues);
                        completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListener.onFailure();
                    }
                });

    }
    public static void loadQuestions(final myCompleteListener completeListener){

        g_questionList.clear();
        g_firestore.collection("Questions").
                whereEqualTo("Category_ID",g_catList.get(g_selectedSubjectIndex).getDocID())
                .whereEqualTo("Test_ID",g_testList.get(g_selectTestIndex).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                         for(QueryDocumentSnapshot doc:queryDocumentSnapshots){
                              g_questionList.add(new QuestionModel(
                                      doc.getId(),
                                      doc.getString("Question"),
                                      doc.getString("A"),
                                      doc.getString("B"),
                                      doc.getString("C"),
                                      doc.getString("D"),
                                      doc.getLong("Answer").intValue()
                                      ));
                         }
                         completeListener.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                            completeListener.onFailure();
                    }
                });

    }



}
