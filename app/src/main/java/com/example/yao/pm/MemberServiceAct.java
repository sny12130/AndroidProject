package com.example.yao.pm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class MemberServiceAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_service);

        TextView tvAndroid123 = (TextView)findViewById(R.id.service_scroll);
        tvAndroid123.setMovementMethod(ScrollingMovementMethod.getInstance());



    }


}


