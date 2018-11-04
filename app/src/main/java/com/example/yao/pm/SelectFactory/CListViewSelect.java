package com.example.yao.pm.SelectFactory;

import android.graphics.Bitmap;

public class CListViewSelect {

    private Bitmap bitmap;

    private String name;

    private String worklocation;

    private String uid;


    public CListViewSelect(Bitmap bitmap, String name, String worklocation,String uid) {
        this.bitmap = bitmap;
        this.name = name;
        this.worklocation = worklocation;
        this.uid = uid;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorklocation() {
        return worklocation;
    }

    public void setWorklocation(String worklocation) {
        this.worklocation = worklocation;
    }

}
