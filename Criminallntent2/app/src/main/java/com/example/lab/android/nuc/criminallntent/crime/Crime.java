package com.example.lab.android.nuc.criminallntent.crime;

import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.UUID;


public class Crime {

    //添加保持不变的Id
    private UUID mId;

    //添加备忘录的标题
    private String mTitle;

    //添加日期
    private Date mDate;

    //用于记录CheckBox的勾选事件
    private boolean mSolved;

    private String mSuspect;


    //构造器
    public Crime() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }

    public String getPhotoFilename(){
        return "IMG_" + getId().toString() + ".jpg";
    }
    //getter和setter方法
    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }


    public boolean isSolved(){
        return mSolved;
    }
    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }
}
