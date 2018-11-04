package com.example.yao.pm;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.example.yao.pm.MapFactory.CListViewMap;
import com.example.yao.pm.MapFactory.ListAdapter;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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



/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    ArrayList<CListViewMap> listmap ;
    private ListAdapter adapter;
    Bitmap pic = null;

    private Handler mUI_Handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 1){
                updateUI();
            }
        }
    };

    private void updateUI(){
        adapter = new ListAdapter(getActivity(),listmap);
        listview地圖.setAdapter(adapter);
    }


    //宣告特約工人的經紀人

    private Handler mThreadHandler;

    //宣告特約工人

    private HandlerThread mThread;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inital();

        mThread = new HandlerThread("name");

        mThread.start();

        mThreadHandler=new Handler(mThread.getLooper());

        mThreadHandler.post(r1);

    }

    private Runnable r1=new Runnable () {

        public void run() {

            HTTP();
            //請經紀人指派工作名稱 r，給工人做

            mUI_Handler.sendEmptyMessage(1);

        }
    };

    public void HTTP(){

        listmap = new ArrayList<CListViewMap>();
        StringBuilder html = new StringBuilder();
        URL url;
        HttpURLConnection conn = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        String urlStr = "";
        try {
            urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/spot";
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
                    String 名子 = forobject.getString("Name");
                    //名子
                    String 描述 = forobject.getString("Description");
                    //描述
                    final String 位置GUID = forobject.getString("GUID");
                    final CListViewMap item = new CListViewMap(pic,名子,描述);
                    final boolean isLast = (i == array.length() -1);
                    new Thread(){
                        //asytask
                        @Override
                        public void run() {
                            super.run();
                            item.setBitmap(Httppicture(位置GUID));
                            if(isLast)
                                mUI_Handler.sendEmptyMessage(1);
                        }
                    }.start();
                    listmap.add(item);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Bitmap Httppicture(String GUID){

        StringBuilder html = new StringBuilder();

        URL url;

        HttpURLConnection conn = null;

        BufferedWriter bw = null;

        BufferedReader br = null;

        String urlStr = "";

        try {
            urlStr = "http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/spot/image?lid=" + GUID;
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
                String count = jsonObject.getString("count");
                if(! "0".equals(count)) {
                    JSONArray array = jsonObject.getJSONArray("data");
                    {
                        JSONObject forobject = array.getJSONObject(0);
                        String path = forobject.getString("Path");
                        URL url1 = new URL(path);
                        URLConnection con1 = url1.openConnection();
                        InputStream streamInn = con1.getInputStream();
                        pic = BitmapFactory.decodeStream(streamInn);
                        return pic;
                    }
                }
                else {
                    pic = null;
                }

            } catch(JSONException e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private View.OnClickListener btn_float_click = new View.OnClickListener() {
        @Override

        public void onClick(View v) {
            //浮動標籤 打開googleMap 定位
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
                startActivityForResult(builder.build(getActivity()), CDictionary.GOOGLEMAP_LOCATION);
            } catch (GooglePlayServicesRepairableException e) {
                e.printStackTrace();
            } catch (GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String locale = "";

        String lalng = "";

        try{

            if (requestCode == CDictionary.GOOGLEMAP_LOCATION) {
                Place place = PlacePicker.getPlace(data, getActivity());

                String 經度 = place.getLatLng().toString();

                String 緯度 = place.getLatLng().toString();

                locale = 經度.substring(經度.indexOf("(")+1,經度.lastIndexOf(","));

                lalng = 緯度.substring(緯度.indexOf(",")+1,緯度.lastIndexOf(")"));

            }

            Bundle bund = new Bundle();

            bund.putString(CDictionary.GOOGLE_LOCALE,locale);

            bund.putString(CDictionary.GOOGLE_LATLNG,lalng);

            Intent intent = new Intent(getActivity(),GoogleMapAdd.class);

            intent.putExtras(bund);

            startActivity(intent);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    private void inital() {
        //初始化 listview && searchview
        listview地圖 = (ListView) getActivity().findViewById(R.id.listview_map);
        floatButtonMap = getActivity().findViewById(R.id.fab_Map);
        floatButtonMap.setOnClickListener(btn_float_click);

    }
    ListView listview地圖;
    FloatingActionButton floatButtonMap;
}