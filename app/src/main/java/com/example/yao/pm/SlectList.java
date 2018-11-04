package com.example.yao.pm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.yao.pm.MemberImage.CImage;
import com.example.yao.pm.MemberImage.GridViewAdapter;
import com.example.yao.pm.SelectFactory.CSlectFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;


public class SlectList extends AppCompatActivity {

    private List<CImage> imagelist = new ArrayList<CImage>();

    private BaseAdapter baseAdapter;

    Bitmap pic = null;

    String userid = "";

    String job = "";

    String  path="";

    String 風格 = "";

    String  電話 ="";

    String 工作地區 ="";



    RoundedBitmapDrawable roundpic = null;

    private Handler mUI_Handler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if(msg.what ==1)
            {
                baseAdapter = new GridViewAdapter(getApplicationContext(),imagelist);
                gridview.setAdapter(baseAdapter);
                gridview.invalidate();

            }
        }
    };


    //宣告特約工人的經紀人

    private Handler mThreadHandler;

    //宣告特約工人



    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_slectlist);

        inital();

        Intent intent = getIntent();

        Bundle bund = intent.getExtras();


        userid = bund.getString(CDictionary.CSLECT_UserID);

        job = bund.getString(CDictionary.CSLECT_JOB);



        HandlerThread  mThread = new HandlerThread("name");

        mThread.start();

        mThreadHandler = new Handler(mThread.getLooper());

        mThreadHandler.post(r1);


        HandlerThread  mThread2 = new HandlerThread("name");

        mThread2.start();

        mThreadHandler = new Handler(mThread2.getLooper());

        mThreadHandler.post(r3);


        HandlerThread  mThread3 = new HandlerThread("name");

        mThread3.start();

        mThreadHandler = new Handler(mThread3.getLooper());

        mThreadHandler.post(r5);


//        initView();


    }

    private Runnable r5= new Runnable () {

        public void run() {

              initView();
              mUI_Handler.sendEmptyMessage(1);

        }
    };




    private Runnable r1=new Runnable () {

        public void run() {

            Profile(job,userid);

            mUI_Handler.post(r2);
        }
    };

    //工作名稱 r2 的工作內容

    private Runnable r2=new Runnable () {

        public void run() {
            lbl風格.setText(風格);
            lbl電話.setText(電話);
            lbl工作地區.setText(工作地區);

        }
    };

    private Runnable r3=new Runnable () {

        public void run() {
            HTTP(userid);
            mUI_Handler.post(r4);

        }
    };


    private Runnable r4 =new Runnable () {

        public void run() {
          img尋覓個人照.setImageDrawable(roundpic);
        }
    };



    public void inital() {

        img尋覓個人照 = findViewById(R.id.img尋覓個人照);

        lbl姓名 = findViewById(R.id.lbl姓名);

        lbl工作地區 = findViewById(R.id.lbl工作地區);

        lbl風格 = findViewById(R.id.lbl風格);

        lbl電話 = findViewById(R.id.lbl電話);

        gridview = (GridView) findViewById(R.id.SelectImageGridView);

        btn聯繫 = findViewById(R.id.btn聯繫聊天);
        btn聯繫.setOnClickListener(btn聯繫_click);

    }


    private View.OnClickListener btn聯繫_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Bundle bund = new Bundle();

            bund.putInt(CDictionary.CSLECT_UserID, Integer.parseInt(userid));
            //這邊是點擊的 uid

            SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);
            String userid = table.getString(CDictionary.UserId_SharedPreferences,"0");

            bund.putInt(CDictionary.CHAT_MYUSERID, Integer.parseInt(userid) );
            //自己的id

            Intent intent = new Intent(SlectList.this,Chat.class);
            intent.putExtras(bund);
            startActivity(intent);

        }
    };




    private void Profile(String job, String userid) {

        if(job.equals("p")) {
            StringBuilder html = new StringBuilder();
            URL url;
            HttpURLConnection conn = null;
            BufferedWriter bw = null;
            BufferedReader br = null;
            try {
                String urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/" + job + "?id=" + userid;

                url = new URL(urlStr);
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                //接收回來的訊息
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                String line;

                while ((line = r.readLine()) != null) {
                    //readline一行一行讀
                    html.append(line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(html.toString());
                    JSONObject object = jsonObject.getJSONObject("data");

                  風格 = object.getString("PhotoType");
                  電話 = object.getString("Phone");
                  工作地區 = object.getString("WorkLocation");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else
        {

            StrictMode.ThreadPolicy l_policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(l_policy);
            StringBuilder html = new StringBuilder();
            URL url;
            HttpURLConnection conn = null;
            BufferedWriter bw = null;
            BufferedReader br = null;
            try {
                String urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/" + job + "?id=" + userid;
                url = new URL(urlStr);
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                //接收回來的訊息
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                String line;

                while ((line = r.readLine()) != null) {
                    //readline一行一行讀
                    html.append(line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(html.toString());
                    JSONObject object = jsonObject.getJSONObject("data");

                    String 風格 = object.getString("AcceptRole");
                    String 電話 = object.getString("Phone");
                    String 工作地區 = object.getString("WorkLocation");

                    lbl風格.setText(風格);
                    lbl電話.setText(電話);
                    lbl工作地區.setText(工作地區);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void initView(){

        //activity 連線圖片方法 && 聯繫gridview
        imagelist.clear();

        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String pp = "";
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
                final JSONArray array = jsonObject.getJSONArray("data");
                for(int i =0;i<array.length();i++)
                {
                    JSONObject json = array.getJSONObject(i);
                    pp = json.getString("Path");
                    final CImage image = new CImage(pic);
                    final String finalPp = pp;

                    final boolean isLast = (i == array.length() -1);
                    new Thread(){
                     @Override
                     public void run() {
                         super.run();
                         image.setBitmap(gridviewPicture(finalPp));
                         if(isLast)
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




    public void HTTP(String userid) {


        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;

        try{
            String urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/user?id=" + userid;
            url = new URL(urlStr);
            URLConnection con = url.openConnection();
            InputStream streamIn=con.getInputStream();
            BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
            String line;
            while ((line = r.readLine()) != null) {
                html.append(line);
            }
            try {
                JSONObject jsonObject = new JSONObject(html.toString());
                JSONObject data = jsonObject.getJSONObject("data");
                path = data.getString("ProfileUrl");
                URL url1 = new URL(path);
                URLConnection con1 = url1.openConnection();
                InputStream streamInn = con1.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(streamInn);
                //圖片 圖片工廠 其中有一個方法可以解串流
                //愛蘋新增
                // set邊框, 影子寬度,  邊框, 影子顏色
                int borderWidth = 5;
                int shadowWidth = 0;
                int borderColor=Color.BLACK;
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

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    ImageView img尋覓個人照;
    TextView  lbl姓名;
    TextView  lbl工作地區;
    TextView  lbl風格;
    TextView  lbl電話;
  GridView gridview ;
  Button btn聯繫;
}

