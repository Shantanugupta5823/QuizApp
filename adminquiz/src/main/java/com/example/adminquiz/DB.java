package com.example.adminquiz;

import android.util.ArrayMap;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DB {
    public static List<SubjectModel> g_catList = new ArrayList();
    public static FirebaseFirestore g_firestore;
    public static int g_selectedSubjectIndex = 0;

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

    public static void deleteSubject(String docID, final myCompleteListener completeListener){

        g_firestore.collection("Quiz ").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Map<String, QueryDocumentSnapshot> doclist = new ArrayMap<>();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            doclist.put(doc.getId(),doc);
                        }
                        QueryDocumentSnapshot catListDoc = doclist.get("Categories");
                         String subId = catListDoc.getString(docID);
                        long catCount = catListDoc.getLong("Count");
                        catCount--;
                        DocumentReference dr =  g_firestore.collection("Quiz ").document("Categories");
                        Map<String,Object> update = new ArrayMap<>();
                        update.put(docID, FieldValue.delete());
                        update.put("Count",catCount);
                        dr.update(update).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                completeListener.onSuccess();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                 completeListener.onFailure();
                            }
                        });
                        g_firestore.collection("Quiz ").document(subId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("TAG","deleted");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Log.d("TAG","Couldnt");
                                    }
                                });
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
