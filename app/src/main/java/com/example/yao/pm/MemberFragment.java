package com.example.yao.pm;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {


    public MemberFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_member, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initial();

    }

    private View.OnClickListener btn會員資料_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(),MemberAct.class);

            startActivity(intent);

        }
    };

    private View.OnClickListener btn版本_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());
            build.setTitle("版本");

            build.setMessage("PM To Be 2.0");

            Dialog message = build.create();

            message.show();

        }
    };

    private View.OnClickListener btn聯絡我們_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            AlertDialog.Builder build = new AlertDialog.Builder(getActivity());

            build.setTitle("Aboout Us");

            build.setMessage("週一至週六 8am-9pm");

            Dialog message = build.create();

            message.show();

        }
    };

    private View.OnClickListener btn問題回報_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(),MemberMailAct.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener btn服務條款_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(getActivity(),MemberServiceAct.class);
            startActivity(intent);
        }
    };
    public void initial(){
        btn會員資料 = getActivity().findViewById(R.id.btn會員資料);
        btn會員資料.setOnClickListener(btn會員資料_click);

        btn版本 = getActivity().findViewById(R.id.btn版本);
        btn版本.setOnClickListener(btn版本_click);

        btn聯絡我們 = getActivity().findViewById(R.id.btn聯絡我們);
        btn聯絡我們.setOnClickListener(btn聯絡我們_click);

        btn問題回報 = getActivity().findViewById(R.id.btn問題回報);
        btn問題回報.setOnClickListener(btn問題回報_click);

        btn服務條款 = getActivity().findViewById(R.id.btn服務條款);
        btn服務條款.setOnClickListener(btn服務條款_click);

    }
    Button btn會員資料;
    Button btn版本;
    Button btn聯絡我們;
    Button btn問題回報;
    Button btn服務條款;
}
