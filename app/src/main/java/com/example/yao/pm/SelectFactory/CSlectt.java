package com.example.yao.pm.SelectFactory;

public class CSlectt {

    private String Uid;

    private String 姓名;
    //攝影師或model
    private String 工作地區;

    private String 風格;

    private String 電話;


    public CSlectt(String uid, String 姓名, String 工作地區, String 風格, String 電話) {
        Uid = uid;
        this.姓名 = 姓名;
        this.工作地區 = 工作地區;
        this.風格 = 風格;
        this.電話 = 電話;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String get姓名() {
        return 姓名;
    }

    public void set姓名(String 姓名) {
        this.姓名 = 姓名;
    }

    public String get工作地區() {
        return 工作地區;
    }

    public void set工作地區(String 工作地區) {
        this.工作地區 = 工作地區;
    }

    public String get風格() {
        return 風格;
    }

    public void set風格(String 風格) {
        this.風格 = 風格;
    }

    public String get電話() {
        return 電話;
    }

    public void set電話(String 電話) {
        this.電話 = 電話;
    }
}



