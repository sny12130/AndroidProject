package com.example.yao.pm.MapFactory;

import android.graphics.Bitmap;

public class CListViewMap {
//這個是存放從網路上抓回來的陣列 set get部分囉~

    private Bitmap bitmap;
    //圖片的url
    private String title;
    //標題
    private String content;
    //內文


    public CListViewMap(Bitmap bitmap, String title, String content) {
        this.bitmap = bitmap;
        this.title = title;
        this.content = content;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
