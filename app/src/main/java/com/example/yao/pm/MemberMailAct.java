package com.example.yao.pm;

import android.app.Dialog;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MemberMailAct extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_mail);

        itinal();

    }

    private View.OnClickListener btn主旨清空_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            edit主旨標題.setText("");
            ediy主旨內容.setText("");
        }
    };

    private View.OnClickListener btn主旨傳送_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            edit主旨標題.setText("");
            ediy主旨內容.setText("");
            AlertDialog.Builder build = new AlertDialog.Builder(MemberMailAct.this);
            build.setMessage("謝謝您的回報,客服人員處理");
            Dialog message = build.create();
            message.show();

        }
    };

    public void itinal() {

        edit主旨標題 = findViewById(R.id.edit主旨標題);

        ediy主旨內容 = findViewById(R.id.edit主旨內容);

        btn主旨傳送 = findViewById(R.id.btn問題傳送);

        btn主旨傳送.setOnClickListener(btn主旨傳送_click);

        btn主旨清空 = findViewById(R.id.btn問題清空);

        btn主旨清空.setOnClickListener(btn主旨清空_click);

    }
    EditText edit主旨標題;

    EditText ediy主旨內容;

    Button btn主旨傳送;

    Button btn主旨清空;

}
