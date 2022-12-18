package com.example.adminquiz.Model;

public class QuestionModel {

    private String docID,ques,optionA,optionB,optionC,optionD;
    private int correctAns;

    public QuestionModel(String docID,String ques, String optionA, String optionB,String optionC, String optionD, int correctAns) {
        this.docID = docID;
        this.ques = ques;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
    }

    public String getQues() {
        return ques;
    }
    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getOptionA() {
        return optionA;
    }
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectAns() {
        return correctAns;
    }
    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }

    public String getDocID() {
        return docID;
    }
    public void setDocID(String docID) {
        this.docID = docID;
    }
}
