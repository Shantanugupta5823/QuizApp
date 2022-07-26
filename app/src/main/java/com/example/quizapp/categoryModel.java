package com.example.quizapp;

public class categoryModel {

    public String docID;
    public String Name;
    public int noOfTest;

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getNoOfTest() {
        return noOfTest;
    }

    public void setNoOfTest(int noOfTest) {
        this.noOfTest = noOfTest;
    }

    public categoryModel(String docID, String name, int noOfTest) {
        this.docID = docID;
        Name = name;
        this.noOfTest = noOfTest;
    }
}
