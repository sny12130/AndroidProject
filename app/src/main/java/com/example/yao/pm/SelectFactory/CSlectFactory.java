package com.example.yao.pm.SelectFactory;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class CSlectFactory {

    private String 職業 = "";

    private String 性別 = "";

    private String 地區 = "";

    Bitmap pic = null;

    ArrayList<CListViewSelect> list = new ArrayList<>();

    int position = 0;

    int bool = 0;


    public CSlectFactory() {
    }

    public void IfSelect(String career, String gender, String location) {
        職業 = career;
        性別 = gender;
        地區 = location;
    }

    public void MoveTo(int index) {
        position = index;
    }

    public CListViewSelect GetCurrent() {
        // list.get(postion)   GET意思是 傳回陣列目前 (index int) 位置的元素值
        return list.get(position);
    }



    public CSlectt[] GetAll() {
        //得到所有的陣列(原本是ArrayList現在變為Array 裡面的大小是size
        return list.toArray(new CSlectt[list.size()]);
    }

    public ArrayList<CListViewSelect> ArrayList() {
        //得到所有的陣列(原本是ArrayList現在變為Array 裡面的大小是size
        return list;
    }

    public int boolUi() {

        return bool;
    }

    public int boolZero() {
        bool = 0;
        return bool;
    }

    public void clear() {
        list.clear();
    }



    public void HTTP(final Handler handler) {

        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String urlStr = null;

        try {
            if( "p".equals(職業) )
            {
                urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/p/find?Gender="+ 性別 + "&Location="+ 地區;
                url = new URL(urlStr);
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                //接收回來的訊息
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                String line;
                while ((line = r.readLine()) != null) {
                    html.append(line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(html.toString());
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject forobject = array.getJSONObject(i);
                        String ShowName = forobject.getString("ShowName");
                        String WorkLocation = forobject.getString("Location");
                        final String Uid = forobject.getString("UserId");
                        final CListViewSelect item = new CListViewSelect(pic,ShowName,WorkLocation,Uid);
                        final boolean isLast = (i == array.length() -1);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                item.setBitmap(bitmapProfileUrl(Uid));
                                if(isLast)
                                  handler.sendEmptyMessage(1);
                            }
                        }.start();
                        list.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else if ("m".equals(職業) )
            {
                urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/m/find?Gender="+ 性別 + "&Location="+ 地區;
                url = new URL(urlStr);
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                //接收回來的訊息
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                String line;
                while ((line = r.readLine()) != null) {
                    html.append(line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(html.toString());
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forobject = array.getJSONObject(i);
                        String ShowName = forobject.getString("ShowName");
                        String WorkLocation = forobject.getString("ModelRole");
                        final String Uid = forobject.getString("UserId");
                        final CListViewSelect item = new CListViewSelect(pic,ShowName,WorkLocation,Uid);

                        final boolean isLast = (i == array.length()-1);

                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                item.setBitmap(bitmapProfileUrl(Uid));
                                if(isLast)
                                    handler.sendEmptyMessage(1);
                            }

                        }.start();
                        list.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap bitmapProfileUrl(String uid) {

        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String urlStr = null;

        try{
            urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/user?id=" + uid;
            url = new URL(urlStr);
            URLConnection con = url.openConnection();
            InputStream streamIn = con.getInputStream();
            //接收回來的訊息
            BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
            String line;
            while ((line = r.readLine()) != null) {
                html.append(line);
            }

            JSONObject jsonObject = new JSONObject(html.toString());
            JSONObject data = jsonObject.getJSONObject("data");
            String path = data.getString("ProfileUrl");


            URL url1 = new URL(path);
            URLConnection con1 = url1.openConnection();
            InputStream streamInn = con1.getInputStream();
            pic = BitmapFactory.decodeStream(streamInn);

            return pic;

            }catch (Exception e){
        }












        return null;
    };


    public void chat(final Handler handler) {


        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String urlStr = null;

        try {
                urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/user";
                url = new URL(urlStr);
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                //接收回來的訊息
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
                String line;
                while ((line = r.readLine()) != null) {
                    html.append(line);
                }
                try {
                    JSONObject jsonObject = new JSONObject(html.toString());
                    JSONArray array = jsonObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject forobject = array.getJSONObject(i);
                        String ShowName = forobject.getString("Username");
                        String WorkLocation = forobject.getString("Email");
                        final String Uid = forobject.getString("UserId");
                        final CListViewSelect item = new CListViewSelect(pic,ShowName,WorkLocation,Uid);
                        final boolean isLast = (i == array.length() -1);
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                item.setBitmap(bitmapProfileUrl(Uid));
                                if(isLast)
                                {
                                    bool ++;
                                    handler.sendEmptyMessage(1);
                                }
                            }
                        }.start();
                        list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void Profile(String UID)
    {
        StrictMode.ThreadPolicy l_policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(l_policy);
        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String urlStr = "";
        try {
            urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/user?id=3" + UID;
            url = new URL(urlStr);
            URLConnection con = url.openConnection();
            InputStream streamIn = con.getInputStream();
            //接收回來的訊息
            BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));
            String line;
            while ((line = r.readLine()) != null) {
                html.append(line);
            }
            try {
                JSONObject jsonObject = new JSONObject(html.toString());
                JSONObject data = jsonObject.getJSONObject("data");

            } catch(JSONException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}