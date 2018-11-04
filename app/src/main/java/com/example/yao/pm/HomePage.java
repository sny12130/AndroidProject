package com.example.yao.pm;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class HomePage extends AppCompatActivity {


    private BottomNavigationView mainnav;
    private FrameLayout mainframe;
    //設置一個主要的 frame 之後可以把其他的Frame裝進去
    private SelectFragment selectFragment;
    private MapFragment mapFragment;
    private ChatFragment chatFragment;
    private MemberFragment memberFragment;
    private HomeFragement homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acthomepage);

        inital();

        come();

        mainnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {  //找尋 menu item id
                    case R.id.nav_find:
                        setFragment(selectFragment);
                        return true;

                    case R.id.nav_map:
                        setFragment(mapFragment);
                        return true;


                   case R.id.nav_chat:
                        setFragment(chatFragment);
                        return true;

                    case R.id.nav_member:
                        setFragment(memberFragment);
                        return true;

                    case R.id.nav_homepage:
                        setFragment(homeFragment);
                        return true;

                    default:
                            return false;

                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        //一個方法設置fragement  取代
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();

    }

    public void come() {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,homeFragment);
        fragmentTransaction.commit();


    }


    public void inital() {


        mainnav = findViewById(R.id.main_nav);
        //註冊 button navigation
        mainframe =findViewById(R.id.main_frame);
        //註冊 frame
        selectFragment =new SelectFragment();

        mapFragment = new MapFragment();

        chatFragment = new ChatFragment();

        memberFragment = new MemberFragment();

        homeFragment = new HomeFragement();

    }

}
