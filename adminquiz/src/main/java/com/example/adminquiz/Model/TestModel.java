package com.example.adminquiz.Model;

public class TestModel {
    private String testId;
    private int maxScore;
    private int time,noOfQuestions;

    public int getNoOfQuestions() {
        return noOfQuestions;
    }

    public void setNoOfQuestions(int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
    }

    public TestModel(String testId, int maxScore, int time, int noOfQuestions) {
        this.testId = testId;
        this.maxScore = maxScore;
        this.time = time;
        this.noOfQuestions = noOfQuestions;
    }

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
