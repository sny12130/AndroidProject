package com.example.yao.pm.MemberImage;

import android.graphics.Bitmap;

public class CImage {

    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public CImage(Bitmap bitmap) {
        this.bitmap = bitmap;

    }
}