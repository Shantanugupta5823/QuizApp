package com.example.quizapp;

import android.util.ArrayMap;
import android.util.Log;


import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class dbQuery {

    public static FirebaseFirestore g_firestore;
    public static List<categoryModel> g_catList = new ArrayList<>();
    public static List<TestModel> g_testList = new ArrayList<>();
    public static List<QuestionModel> g_questionList = new ArrayList<>();
    public static int g_SelectedTestIndex = 0;
    public static int g_selectedCatIndex = 0;
    public static profileModel myProfile = new profileModel("NA",null);

    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;


    public static void createUserData(String Email, String Name,final myCompleteListner myCompleteListner) {

        Map<String, Object> userData = new ArrayMap<>();

        userData.put("Email_ID", Email);
        userData.put("Name", Name);
        userData.put("Top_Score", 0);

        DocumentReference userDoc = g_firestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        WriteBatch batch = g_firestore.batch();
        batch.set(userDoc, userData);

        DocumentReference countDoc = g_firestore.collection("User").document("Total_Users");
        batch.update(countDoc, "Count", FieldValue.increment(1));

        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                myCompleteListner.onSuccess();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        myCompleteListner.onFailure();
                    }
                });
    }

    public static void loadCategories(final myCompleteListner completeListner){
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
                            Log.d("NoOfTEsts",String.valueOf(noOfTests));
                            String categoryName  = catDoc.getString("Name ");
                            g_catList.add(new categoryModel(catID,categoryName,noOfTests));
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

    public static void loadTestData(final myCompleteListner completeListner){
        g_testList.clear();
        g_firestore.collection("Quiz ").document(g_catList.get(g_selectedCatIndex).getDocID())
                .collection("Test_List").document("Tests_Info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selectedCatIndex).getNoOfTest();
                        for(int i = 1; i<= noOfTests; i++){

                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("Test"+String.valueOf(i)+"_ID"),
                                    0,
                                    documentSnapshot.getLong("Test"+String.valueOf(i)+"_Time").intValue()
                            ));

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

    public static void getUserData(final myCompleteListner completeListner){
        g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        myProfile.setName(documentSnapshot.getString("Name"));
                        myProfile.setEmail(documentSnapshot.getString("Email_ID"));
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

    public static void loadData(final myCompleteListner completeListner){
            loadCategories(new myCompleteListner() {
                @Override
                public void onSuccess() {
                     getUserData(completeListner);
                }
                @Override
                public void onFailure() {
                    completeListner.onFailure();
                }
            });
    }

    public static void loadQuestions(myCompleteListner completeListner){
        g_questionList.clear();
        g_firestore.collection("Questions")
                .whereEqualTo("Category_ID",g_catList.get(g_selectedCatIndex).getDocID())
                .whereEqualTo("Test_ID",g_testList.get(g_SelectedTestIndex).getTestId())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(DocumentSnapshot doc : queryDocumentSnapshots){
                            g_questionList.add(new QuestionModel(
                                    doc.getString("Question"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("Answer").intValue(),
                                    -1,NOT_VISITED
                            ));
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

}

