package com.example.yao.pm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yao.pm.MemberImage.CImage;
import com.example.yao.pm.MemberImage.GridViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MemberAct extends AppCompatActivity {

    private List<CImage> imagelist = new ArrayList<CImage>();

    private BaseAdapter baseAdapter;

    Bitmap pic = null;

    String  path="";

    RoundedBitmapDrawable roundpic = null;

    private Handler mUI_Handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(msg.what ==1)
            {
                baseAdapter = new GridViewAdapter(MemberAct.this,imagelist);
                grid會員相片.setAdapter(baseAdapter);
                grid會員相片.invalidate();
            }
        }
    };

    //宣告特約工人的經紀人

    private Handler mThreadHandler;

    //宣告特約工人





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_member);

        inital();

        HandlerThread mThread2 = new HandlerThread("name");

        mThread2.start();

        mThreadHandler = new Handler(mThread2.getLooper());

        mThreadHandler.post(r3);


        ///

        HandlerThread mThread5 = new HandlerThread("name");

        mThread5.start();

        mThreadHandler = new Handler(mThread5.getLooper());

        mThreadHandler.post(r5);
    }

    private Runnable r5=new Runnable () {

        public void run() {

            initView();
            mUI_Handler.sendEmptyMessage(1);
        }
    };

    private Runnable r3 =new Runnable () {

        public void run() {
            HTTP();
            mUI_Handler.post(r4);

        }
    };

    private Runnable r4 =new Runnable () {

        public void run() {
            img使用者大頭貼.setImageDrawable(roundpic);
        }
    };


    public void initView(){

        //activity 連線圖片方法 && 聯繫gridview
        imagelist.clear();

        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        String pp = "";

        SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);
        String   userid = table.getString(CDictionary.UserId_SharedPreferences,"0");

        try{
            String urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/IMAGE?uid=" + userid;
            url = new URL(urlStr);
            URLConnection con = url.openConnection();
            InputStream streamIn=con.getInputStream();
            //接收回來的訊息
            BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
            String line;

            while ((line = r.readLine()) != null) {
                //readline一行一行讀
                html.append(line);
            }
            try {
                JSONObject jsonObject = new JSONObject(html.toString());
                JSONArray array = jsonObject.getJSONArray("data");
                for(int i =0;i<array.length();i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    pp = json.getString("Path");
                    final CImage image = new CImage(pic);
                    final String finalPp = pp;
                    final boolean ifLast = (i==array.length()-1);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            image.setBitmap(gridviewPicture(finalPp));
                            if(ifLast)
                                mUI_Handler.sendEmptyMessage(1);
                        }

                    }.start();

                    imagelist.add(image);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private Bitmap gridviewPicture(String path) {


        try{
            URL url1 = new URL(path);
            URLConnection con1 = url1.openConnection();
            InputStream streamInn = con1.getInputStream();
            pic = BitmapFactory.decodeStream(streamInn);
            int width = pic.getWidth();
            int height = pic.getHeight();
            int newWidth=130;
            int newHeight=130;
            float scaleWidth=((float)newWidth)/width;
            float scaleHeight=((float)newHeight)/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth,scaleHeight);
            pic=Bitmap.createBitmap(pic,0,0,width,height,matrix,true);
            pic.getWidth();
            pic.getHeight();
            return pic;

        }catch (Exception e)
        {
        }
        return null;
    }

    private View.OnClickListener btn登出_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //登出 把SHAREPREFERENCES 給清空~設為0 (一開始設為0就是不進入這一頁
            SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);

            table.edit().putString(CDictionary.Account_SharedPreferences,"0").commit();

            table.edit().putString(CDictionary.UserId_SharedPreferences,"0").commit();

            Intent intent = new Intent(MemberAct.this,ActMain.class);
            startActivity(intent);
        }
    };

    public void HTTP() {
        //get方法
        //限制不用使用執 行序
        String email ="";
        String path="";
        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        try{
            SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);
            String   userid = table.getString(CDictionary.UserId_SharedPreferences,"0");
            String account = table.getString(CDictionary.Account_SharedPreferences,"0");


            String urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/user?id=" + userid;
            url = new URL(urlStr);
            URLConnection con = url.openConnection();
            InputStream streamIn=con.getInputStream();
            //接收回來的訊息
            BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
            String line;
            while ((line = r.readLine()) != null) {
                //readline一行一行讀
                html.append(line);
            }
            try {
                JSONObject jsonObject = new JSONObject(html.toString());
                JSONObject data = jsonObject.getJSONObject("data");

                path = data.getString("ProfileUrl");
                email = data.getString("Email");
                String username = data.getString("Username");
                lbl使用者帳號.setText(username);
                lbl伊媚兒.setText(email);
                URL url1 = new URL(path);
                URLConnection con1 = url1.openConnection();
                InputStream streamInn = con1.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(streamInn);
                //圖片 圖片工廠 其中有一個方法可以解串流

                int borderWidth = 5;
                int shadowWidth = 0;
                int borderColor= Color.BLACK;
                int shadowColor=Color.DKGRAY;

                // 以圖片大小為尺寸, 建立一塊畫布, 取得圖片長跟寬
                int picWidth=pic.getWidth();
                int picHight=pic.getHeight();
                int picTotalWidth=Math.min(picWidth, picHight)+borderWidth*2;
                // 建立一塊畫布

                Log.d("QQ", "畫布");

                Bitmap picTotal = Bitmap.createBitmap(picTotalWidth,picTotalWidth, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(picTotal);
                canvas.drawColor(Color.WHITE);
                canvas.drawBitmap(pic, (picTotalWidth- picWidth)/2, (picTotalWidth-picHight)/2, null);

                Log.d("QQ", "畫布2");
                // 在畫布上畫邊線

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(borderWidth*2);
                paint.setColor(borderColor);
                canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/2, paint);
                // 在畫布上畫影子
                paint.setColor(shadowColor);
                paint.setStrokeWidth(shadowWidth);
                canvas.drawCircle(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth()/2, paint);

                Log.d("QQ", "影子");
                // 將圖片切圓角

                roundpic = RoundedBitmapDrawableFactory.create(null, picTotal);
                roundpic.setCircular(true);

                Log.d("QQ", "切圓角");
                // 將轉好的圖貼在imageView中




            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void inital(){

        lbl使用者帳號 = findViewById(R.id.lbl使用者帳號);
        btn登出 = findViewById(R.id.btn登出);
        btn登出.setOnClickListener(btn登出_click);
        img使用者大頭貼 = findViewById(R.id.img使用者大頭貼);
        lbl伊媚兒 = findViewById(R.id.lbl伊媚兒);
        grid會員相片 = findViewById(R.id.grid會員相片);
    }

    TextView lbl使用者帳號;
    TextView lbl伊媚兒;
    Button btn登出;
    ImageView img使用者大頭貼;
    GridView grid會員相片;
}

