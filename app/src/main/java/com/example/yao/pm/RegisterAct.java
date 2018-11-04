package com.example.yao.pm;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegisterAct extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_register);

        inital();

    }

    private View.OnClickListener btn註冊_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            StrictMode.ThreadPolicy l_policy =  new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(l_policy);
            //強制關閉執行序
            StringBuilder html = new StringBuilder();
            URL url;
            HttpURLConnection conn = null;
            BufferedWriter bw = null;
            BufferedReader br = null;


            try{
                String urlStr = String.format("http://ec2-54-250-199-234.ap-northeast-1.compute.amazonaws.com/api/Register");
                //註冊的網址
                url = new URL(urlStr);
                conn = (HttpURLConnection) url.openConnection();

                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                //方法用post
                conn.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=utf-8");
                //編碼 UTF-8

                OutputStream os = conn.getOutputStream();
                //傳出去的串流 是
                bw = new BufferedWriter( new OutputStreamWriter(os,"utf-8") );

                String write = String.format("Username=%s&Password=%s&Fname=%s&Email=%s",
                        tb帳號.getText().toString(),
                        tb密碼.getText().toString(),
                        tb姓名.getText().toString(),
                        tb信箱.getText().toString()
                );

                os.write(write.getBytes());

                bw.flush();
                bw.close();

                InputStream streamIn=conn.getInputStream();
                //stream 收回來了
                BufferedReader r = new BufferedReader(new InputStreamReader(streamIn));

                String line;
                while ((line = r.readLine()) != null) {
                    html.append(line);
                }


                Intent intent = new Intent(RegisterAct.this,ActMain.class);
                startActivity(intent);
                Toast.makeText(RegisterAct.this,"註冊成功", Toast.LENGTH_LONG).show();
            }

            catch(Exception e){
                e.printStackTrace();
            }
        }
    };

    private View.OnClickListener btn清空欄位_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            tb帳號.setText("");

            tb密碼.setText("");

            tb姓名.setText("");

            tb信箱.setText("");

        }
    };

    private void inital() {

        btn註冊 = findViewById(R.id.btn註冊);
        btn註冊.setOnClickListener(btn註冊_click);

        btn清空欄位 = findViewById(R.id.btn清空欄位);
        btn清空欄位.setOnClickListener(btn清空欄位_click);

        tb帳號 = findViewById(R.id.tb帳號);

        tb密碼 = findViewById(R.id. tb密碼);

        tb姓名 = findViewById(R.id.tb姓名);

        tb信箱 = findViewById(R.id.tb信箱);


    }

    Button btn註冊;
    Button btn清空欄位;
    EditText tb帳號;
    EditText tb密碼;
    EditText tb姓名;
    EditText tb信箱;

}
