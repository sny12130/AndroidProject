package com.example.yao.pm;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.yao.pm.SelectFactory.CSlectFactory;
import com.example.yao.pm.SelectFactory.SelectListAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    private SelectListAdapter adapter;
    Bitmap pic = null;
    CSlectFactory factory = new CSlectFactory();


    private Handler mUI_Handler = new Handler();


    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inital();



        new Thread(){
            @Override
            public void run() {

                chatlist();
            }
        }.start();

    }


    private Handler r2=new Handler () {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what ==1) {
                adapter = new SelectListAdapter(getActivity(), factory.ArrayList());
                listview聊天.setAdapter(adapter);
                listview聊天.invalidate();
            }
        }
    };



    public void chatlist(){

        factory.clear();

        factory.chat(r2);


        listview聊天.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                factory.MoveTo(position);

                factory.GetCurrent();

                String Uid = factory.GetCurrent().getUid();

                Bundle bund = new Bundle();

                bund.putInt(CDictionary.CSLECT_UserID, Integer.parseInt(Uid));
                //這邊是點擊的 uid

                SharedPreferences table = getActivity().getSharedPreferences(CDictionary.Account_table,0);
                String userid = table.getString(CDictionary.UserId_SharedPreferences,"0");

                bund.putInt(CDictionary.CHAT_MYUSERID, Integer.parseInt(userid) );
                //自己的id

                Intent intent = new Intent(getActivity(),Chat.class);
                intent.putExtras(bund);
                startActivity(intent);
            }
        });
    }

    public void inital()

    //初始化
    {
        listview聊天 = (ListView) getActivity().findViewById(R.id.listview聊天列表);
    }

    ListView listview聊天;
}
