package com.example.yao.pm;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.fido.fido2.api.common.RequestOptions;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragement extends Fragment {


    public HomeFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);

      


    }


}
