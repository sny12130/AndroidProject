package com.example.yao.pm;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chat extends AppCompatActivity {

    ArrayList<IMEntity> messageList= new ArrayList<>();
    ChatAdapter adapter;
    EmptyClient client = null;
    Handler handler;
    public String domain = "http://54.250.199.234/";
    //請重點關注這邊
    //目標uid
    public int targetUid = 2;
    //使用者自己的uid
    public int localUid = 1;
    //應用時請自行使用讓程式修改上述兩項
    MessageRoomSchema mrs = new MessageRoomSchema();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_imroom);
        initComponent();
        initHandler();
        setTitle("對話");

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        //get bundle

        Intent intent = getIntent();

        Bundle bund = intent.getExtras();

        localUid = bund.getInt(CDictionary.CHAT_MYUSERID);

        targetUid = bund.getInt(CDictionary.CSLECT_UserID);



        messageList = new ArrayList<>();
        IMEntity imEntity = null;

        // adapter

        adapter= new ChatAdapter(this,messageList);
        msgListView.setAdapter(adapter);


        getRoomSchema();
        client = NetworkFactory.touchMessageSocket(localUid,handler);
        getInitMessage();
        //

    }

    //click event

    View.OnClickListener btnSend_Click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //here is send action
            sendMessage(txtMsg.getText().toString());
            txtMsg.setText("");

        }
    };
    //method



    private View.OnClickListener btn語音_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //開啟語音

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說0.0");
            try{
                startActivityForResult(intent,200);
            }catch (ActivityNotFoundException a){
                Toast.makeText(getApplicationContext(),"Intent problem", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收語音

        if(requestCode == 200){
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                txtMsg.setText(result.get(0));
            }
        }
    }



    public void getRoomSchema(){

        final String urlStr = domain+"api/message/IMInfo?accessuid="+localUid+"&target="+targetUid;
        new Thread(){
            @Override
            public void run() {
                String result = NetworkFactory.httpGet(urlStr);
                mrs.get(result);
                mrs.local.downloadProfile();
                mrs.target.downloadProfile();
            }

        }.start();
    }

    public void getInitMessage(){

        final String urlStr = domain+"api/message/IM?accessuid="+localUid+"&target="+targetUid;
        new Thread(){
            @Override
            public void run() {
                String result = NetworkFactory.httpGet(urlStr);
                ArrayList<IMEntity> list = IMFactory.createEntity(result,mrs,true);

                for (int i = 0 ; i < list.size() ; i++)
                {
                    appendMessage(list.get(i));
                }
                Message msg = new Message();
                msg.what = 1;
                refreshHandler.sendMessage(msg);

            }

        }.start();
    }

    Handler refreshHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try{
                if(msg.what == 1)
                {
                    msgListView.invalidate();
                }
            }
            catch (Exception ex)
            {
                Log.d("testHandler",ex.getLocalizedMessage());
            }
        }
    };


    private void sendMessage(String message){
        try{
            Map<String,String> map = new HashMap() {};
            map.put("Receiver",String.valueOf(mrs.getTarget().getUid()));
            map.put("Message",message);
            String postUrl = domain+"api/message/im?accessuid="+localUid+"&savedb=1";
            NetworkFactory.httpConnectionPost(postUrl,map,handler);
        }
        catch (Exception ex){
            Log.d("testSend",ex.getLocalizedMessage());
        }
    }

    private void appendMessage(IMEntity chatEntity){
        try{
            messageList.add(chatEntity);
            adapter.notifyDataSetChanged();
            msgListView.setSelection(messageList.size() - 1);
        }
        catch (Exception ex)
        {
            Log.d("append",ex.getLocalizedMessage());
        }
    }


    public void ApplyMsg(String str){
        txtMsg.setText(str);
    }

    //
    public void initComponent(){
        btnSend = findViewById(R.id.btnSend);
        txtMsg = findViewById(R.id.txtMsg);
        btnSend.setOnClickListener(btnSend_Click);
        msgListView = findViewById(R.id.msglist);

        btn語音 = findViewById(R.id.btn自動語音);
        btn語音.setOnClickListener(btn語音_click);



    }

    public void initHandler(){
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                try{
                    if(msg.what == 1)
                    {
                        String json = (String) msg.obj;
                        Log.d("testHandlerjson",json);
                        ArrayList<IMEntity> list = IMFactory.createEntityFromSocket(json,mrs);
                        for (int i = 0 ; i < list.size() ; i++)
                        {
                            appendMessage(list.get(i));
                            Log.d("testHandlerjson",list.get(i).getMessage());
                        }
                    }
                    msgListView.invalidate();
                }
                catch (Exception ex)
                {
                    Log.d("testHandler",ex.getLocalizedMessage());
                }
            }
        };
    }





    Button btnSend;
    EditText txtMsg;
    ListView msgListView;
    Button btn語音;


    //following is adapter

    private class ChatAdapter extends BaseAdapter {
        private Context context = null;
        private List<IMEntity> chatList = null;
        private LayoutInflater inflater = null;
        private int COME_MSG = 0;
        private int TO_MSG = 1;

        public ChatAdapter(Context context,List<IMEntity> chatList){
            this.context = context;
            this.chatList = chatList;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return chatList.size();
        }

        @Override
        public Object getItem(int position) {
            return chatList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // 設定類型的layout
            IMEntity entity = chatList.get(position);
            if (entity.isLocal())
            {
                return TO_MSG;
            }else{
                return COME_MSG;
            }
        }

        @Override
        public int getViewTypeCount() {
            // 兩種類型，設為2
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatHolder chatHolder = null;
            if (convertView == null) {
                chatHolder = new ChatHolder();
                if (chatList.get(position).isLocal()) {
                    convertView = inflater.inflate(R.layout.msg_item_local, null);
                }else {
                    convertView = inflater.inflate(R.layout.msg_item_come, null);
                }
                //chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.tv_time);
                chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.msg_text);
                chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.msg_upi);
                convertView.setTag(chatHolder);
            }else {
                chatHolder = (ChatHolder)convertView.getTag();
            }

            //chatHolder.timeTextView.setText(chatList.get(position).getChatTime());
            chatHolder.contentTextView.setText(chatList.get(position).getMessage());
            try{
                if(chatList.get(position)!=null)
                {
                    if(chatList.get(position).getProfile() != null)
                    {
                        chatHolder.userImageView.setImageBitmap(chatList.get(position).getProfile());

                    }
                }
            }
            catch (Exception ex)
            {
                Log.d("test",ex.getLocalizedMessage());
            }

            return convertView;
        }

        private class ChatHolder{
            private TextView timeTextView;
            private ImageView userImageView;
            private TextView contentTextView;
        }

    }

}

class HandlerFlag{
    public final int IM_APPEND = 1;
    public final int IM_INIT_MESSAGE = 2;
}




