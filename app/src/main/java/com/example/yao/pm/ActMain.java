package com.example.yao.pm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yao.pm.SelectFactory.CSlectFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class ActMain extends AppCompatActivity {
//登入頁
// 紀錄jsonObject得值

    String result = "";
    String id = "";

    private View.OnClickListener btn已有帳號登入_click  = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //帳號登入紀錄帳號

            AlertDialog.Builder builder = new AlertDialog.Builder(ActMain.this);
            View view = getLayoutInflater().inflate(R.layout.login_dialog,null);
            tb密碼登入 = view.findViewById(R.id.tb密碼登入);
            tb帳號登入 = view.findViewById(R.id.tb帳號登入);
            //alertdialog 找findViewByid 記得要 view!!

            builder.setPositiveButton("登入", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                //Dialog的確認按鈕 以及紀錄東西

                    StrictMode.ThreadPolicy l_policy =  new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(l_policy);

                    StringBuilder html = new StringBuilder();
                    URL url;
                    HttpURLConnection conn = null;
                    BufferedWriter bw = null;
                    BufferedReader br = null;

                    try {
                        String urlStr = String.format("http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/login");
                        //不能用Lohost 因為安卓本身就是LoalHost

                        url = new URL(urlStr);conn = (HttpURLConnection)url.openConnection();
                        conn.setDoOutput(true);
                        conn.setDoInput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=utf-8");

                        OutputStream os = conn.getOutputStream();
                        bw = new BufferedWriter( new OutputStreamWriter(os,"utf-8") );

                        String account = tb帳號登入.getText().toString();
                        String password = tb密碼登入.getText().toString();

                        String write = "Username="+ account + "&" + "Password=" + password;

                        os.write(write.getBytes());
                        //接收端取password就會給abc了
                        //  string asdasdas =request.form["id"]
                        bw.flush();
                        bw.close();

                        InputStream streamIn=conn.getInputStream();
                        //stream 收回來了
                        BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));

                            String line;
                            while ((line = r.readLine()) != null) {
                                //readline一行一行讀
                                html.append(line);
                            }


                        try {
                            JSONObject jsonObject = new JSONObject(html.toString());
                            //jsonobject的解析
                            result = jsonObject.getString("result");

                            JSONObject data = jsonObject.getJSONObject("data");

                            id = data.getString("id");


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if("-1".equals(result))
                            {
                                Toast.makeText(ActMain.this,"帳號密碼錯誤請重新登入", Toast.LENGTH_LONG).show();

                            }
                            else{
                              Intent intent = new Intent(ActMain.this,HomePage.class);
                               startActivity(intent);

                                //存進去SharedPreferences

                            }

                        SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);

                        table.edit().putString(CDictionary.Account_SharedPreferences,account).commit();

                        table.edit().putString(CDictionary.UserId_SharedPreferences,id).commit();


                    }catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                    }
           });
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private View.OnClickListener btn建立帳號_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ActMain.this,RegisterAct.class);
            startActivity(intent);
            //進入建立帳號的page
        }
    };

    private View.OnClickListener btn免稅入關_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(ActMain.this,HomePage.class);

            startActivity(intent);

            //強迫直接駭進去
        }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actmain);
        inital();
        //註冊初始化

        //登入的時候檢查 如果 session 為0的話不跳轉~
        SharedPreferences table = getSharedPreferences(CDictionary.Account_table,0);

        String userid = table.getString(CDictionary.UserId_SharedPreferences,"0");

        if("0".equals(userid)){

        }else{

            Intent intent = new Intent(ActMain.this,HomePage.class);
            startActivity(intent);
        }
    }

    public void inital (){



        lblpm = findViewById(R.id.lblpm);

        btn建立帳號 = findViewById(R.id.btn建立帳號);
        btn建立帳號.setOnClickListener(btn建立帳號_click);

        btn已有帳號登入 = findViewById(R.id.btn已有帳號登入);
        btn已有帳號登入.setOnClickListener(btn已有帳號登入_click);

        btn免稅入關 = findViewById(R.id.btn免稅入關);
        btn免稅入關.setOnClickListener(btn免稅入關_click);

    }

    Button btn建立帳號;
    Button btn已有帳號登入;
    TextView lblpm;
    EditText tb帳號登入;
    EditText tb密碼登入;
    Button btn免稅入關;

}
