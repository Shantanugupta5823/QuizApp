package com.example.adminquiz;

public class SubjectModel {

    private String Name,docID;
    private int noOfTests;

    public SubjectModel(String docID,String name,int noOfTests) {
        Name = name;
        this.docID = docID;
        this.noOfTests = noOfTests;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public int getNoOfTests() {
        return noOfTests;
    }

    public void setNoOfTests(int noOfTests) {
        this.noOfTests = noOfTests;
    }
}
