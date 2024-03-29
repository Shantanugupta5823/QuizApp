package com.example.quizapp.DataBase;

import android.util.ArrayMap;
import android.util.Log;


import androidx.annotation.NonNull;

import com.example.quizapp.model.QuestionModel;
import com.example.quizapp.model.RankModel;
import com.example.quizapp.model.TestModel;
import com.example.quizapp.model.categoryModel;
import com.example.quizapp.model.profileModel;
import com.example.quizapp.myCompleteListner;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
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
    public static List<RankModel> g_usersList = new ArrayList<>();
    public static int g_usersCount = 0;
    public static boolean isMeOnTop = false;
    public static int g_SelectedTestIndex = 0;
    public static int g_selectedCatIndex = 0;
    public static profileModel myProfile = new profileModel("NA",null,null,0);
    public static RankModel myPerformance = new RankModel(0,-1,"NULL");
    public static List<String> g_bmIdList = new ArrayList<>();
    public static List<QuestionModel> g_bookmarksList = new ArrayList<>();
    public static final int NOT_VISITED = 0;
    public static final int UNANSWERED = 1;
    public static final int ANSWERED = 2;
    public static final int REVIEW = 3;
    public static int temp = 0;


    public static void loadBookmarks(myCompleteListner completeListner){
        g_bookmarksList.clear();
        temp = 0;
        if(g_bmIdList.size() == 0){
            completeListner.onSuccess();
        }
        for(int i =0; i<g_bmIdList.size(); i++){

            String docId = g_bmIdList.get(i);

            g_firestore.collection("Questions").document(docId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()){
                                g_bookmarksList.add(new QuestionModel(
                                        documentSnapshot.getId(),
                                        documentSnapshot.getString("Question"),
                                        documentSnapshot.getString("A"),
                                        documentSnapshot.getString("B"),
                                        documentSnapshot.getString("C"),
                                        documentSnapshot.getString("D"),
                                        documentSnapshot.getLong("Answer").intValue(),
                                        0,UNANSWERED,false
                                ));
                            }
                            temp++;
                            if(temp == g_bmIdList.size()){
                                completeListner.onSuccess();
                            }
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

    public static void getTopUsers(myCompleteListner completeListner){
        g_usersList.clear();
        String myUID = FirebaseAuth.getInstance().getUid();

        g_firestore.collection("User")
                .whereGreaterThan("Top_Score",0)
                .orderBy("Top_Score", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int rank = 1;
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            g_usersList.add(new RankModel(
                                    doc.getLong("Top_Score").intValue(),
                                    rank,
                                    doc.getString("Name")
                            ));
                            if(myUID.compareTo(doc.getId()) == 0){
                                isMeOnTop = true;
                                myPerformance.setRank(rank);
                            }
                            rank++;
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

    public static void getUsersCount(myCompleteListner completeListner){
        g_firestore.collection("User").document("Total_Users")
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        completeListner.onFailure();
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        g_usersCount = documentSnapshot.getLong("Count").intValue();

                        completeListner.onSuccess();
                    }
                });
    }

    public static void createUserData(String Email, String Name,final myCompleteListner myCompleteListner) {

        Map<String, Object> userData = new ArrayMap<>();

        userData.put("Email_ID", Email);
        userData.put("Name", Name);
        userData.put("Top_Score", 0);
        userData.put("BOOKMARKS",0);

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
                .collection("Test_List").document("Test_Info")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int noOfTests = g_catList.get(g_selectedCatIndex).getNoOfTest();
                        for(int i = 1; i<= noOfTests; i++){
                            g_testList.add(new TestModel(
                                    documentSnapshot.getString("Test_"+String.valueOf(i)+"_ID"),
                                    Integer.valueOf(String.valueOf(documentSnapshot.getLong("Test_" + String.valueOf(i) + "_MaxScore"))),
                                    documentSnapshot.getLong("Test_"+String.valueOf(i)+"_Time").intValue()
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
                        myPerformance.setName(documentSnapshot.getString("Name"));
                        myProfile.setEmail(documentSnapshot.getString("Email_ID"));
                        if(documentSnapshot.getString("PHONE") != null){
                            myProfile.setPhoneNo(documentSnapshot.getString("PHONE"));
                        }
                        if(documentSnapshot.get("BOOKMARKS") != null){
                            myProfile.setBookmarkCount(documentSnapshot.getLong("BOOKMARKS").intValue());
                        }
                        myPerformance.setScore(documentSnapshot.getLong("Top_Score").intValue());
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
                     getUserData(new myCompleteListner() {
                         @Override
                         public void onSuccess() {
                             getUsersCount(new myCompleteListner() {
                                 @Override
                                 public void onSuccess() {
                                     loadBmIds(completeListner);
                                 }
                                 @Override
                                 public void onFailure() {
                                    completeListner.onFailure();
                                 }
                             });
                         }
                         @Override
                         public void onFailure() {
                            completeListner.onFailure();
                         }
                     });
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
                .whereEqualTo("Test_ID",g_testList.get(g_SelectedTestIndex).getTestId()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            boolean isBookMarked = false;
                            if (g_bmIdList.contains(doc.getId())) {
                                isBookMarked = true;
                            }
                            g_questionList.add(new QuestionModel(
                                    doc.getId(),
                                    doc.getString("Question"),
                                    doc.getString("A"),
                                    doc.getString("B"),
                                    doc.getString("C"),
                                    doc.getString("D"),
                                    doc.getLong("Answer").intValue(),
                                    -1,NOT_VISITED,isBookMarked
                            ));
                        }
                        completeListner.onSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d("Hello","Loading Test Failed");
                    }
                });
    }

    public static void loadMyScore(myCompleteListner completeListner){
        g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_SCORES")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        for(int i = 0; i<g_testList.size(); i++){
                            int top = 0;
                            if(documentSnapshot.get(g_testList.get(i).getTestId()) != null){
                                top = documentSnapshot.getLong(g_testList.get(i).getTestId()).intValue();
                            }
                            g_testList.get(i).setTopScore(top);
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

    public static void saveResult(int score, myCompleteListner completeListner){
        WriteBatch batch = g_firestore.batch();

//        BookMarks
        Map<String,Object> bmData = new ArrayMap<>();

        for(int i = 0; i<g_bmIdList.size(); i++){
            bmData.put("BM"+ String.valueOf(i+1)+"_ID",g_bmIdList.get(i));
        }

        DocumentReference bmDoc = g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                                    .collection("USER_DATA").document("BOOKMARKS");
        batch.set(bmDoc,bmData);

        Map<String,Object> userData = new ArrayMap<>();
        userData.put("Top_Score",score);
        userData.put("BOOKMARKS",g_bmIdList.size());
        DocumentReference userDoc = g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid());
        batch.update(userDoc, userData);


        if(score > g_testList.get(g_SelectedTestIndex).getTopScore()){
            DocumentReference scoreDoc = userDoc.collection("USER_DATA").document("MY_SCORES");
            Map<String, Object> testData = new ArrayMap<>();
            testData.put(g_testList.get(g_SelectedTestIndex).getTestId(),score);
            batch.set(scoreDoc,testData, SetOptions.merge());

        }
        batch.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if(score > g_testList.get(g_SelectedTestIndex).getTopScore()){
                    g_testList.get(g_SelectedTestIndex).setTopScore(score);
                }
                myPerformance.setScore(score);
                completeListner.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                completeListner.onFailure();
            }
        });
    }

    public static void saveProfileData(String name, String PhoneNo, myCompleteListner completeListner){
        Map<String,Object> profileData = new ArrayMap<>();
        profileData.put("Name",name);
        profileData.put("Phone Number",PhoneNo);

        g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                .update(profileData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        myProfile.setName(name);
                        myProfile.setPhoneNo(PhoneNo);
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

    public static void loadBmIds(myCompleteListner completeListner){
        g_bmIdList.clear();

        g_firestore.collection("User").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("BOOKMARKS")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int count = myProfile.getBookmarkCount();

                        for(int i = 0; i<count; i++){
                            String bmId = documentSnapshot.getString("BM" + String.valueOf(i+1)+ "_ID");
                            g_bmIdList.add(bmId);
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

