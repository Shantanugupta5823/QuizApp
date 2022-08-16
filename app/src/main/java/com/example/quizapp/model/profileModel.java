package com.example.quizapp.model;

public class profileModel {

    private String name;
    private String email;
    private String phoneNo;
    private int bookmarkCount;

    public profileModel(String name, String email, String phoneNo,int bookmarkCount) {
        this.name = name;
        this.email = email;
        this.phoneNo = phoneNo;
        this.bookmarkCount = bookmarkCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }
}
