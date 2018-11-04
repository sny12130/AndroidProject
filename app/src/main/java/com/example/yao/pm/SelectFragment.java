package com.example.yao.pm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.yao.pm.MapFactory.ListAdapter;
import com.example.yao.pm.SelectFactory.CSlectFactory;
import com.example.yao.pm.SelectFactory.SelectListAdapter;

import static java.lang.Thread.sleep;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFragment extends Fragment {

    //    ArrayList<CListViewSelect> listselect ;
    private SelectListAdapter adapter;
    Bitmap pic = null;
    CSlectFactory factory = new CSlectFactory();
    String 職業="";
    String 性別="";
    String 地區="";

    private Handler mUI_Handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==1){
                updateUi();

            }
        }
    };



    public SelectFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        inital();

    }

    private View.OnClickListener btn尋人_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            final View view = getLayoutInflater().inflate(R.layout.select_dialog,null);

            final EditText tb尋人地區 = view.findViewById(R.id.tb地區);

            final RadioGroup radiogroup_攝影模特 = view.findViewById(R.id.radiogroup_攝影模特);

            final RadioGroup radiogroup_性別 = view.findViewById(R.id.radiogroup_性別);
            //找到 radio群組的設定


            builder.setPositiveButton("Pair", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    try{
                        switch (radiogroup_攝影模特.getCheckedRadioButtonId())
                        //選擇group群組 然後設定值
                        {
                            case R.id.rabtn_攝影師:
                                職業 = "p";
                                break;
                            case R.id.rabtn_模特爾:
                                職業 ="m";
                                break;
                        }
                        switch (radiogroup_性別.getCheckedRadioButtonId())
                        //選擇group群組 然後設定值
                        {
                            case R.id.rabtn_男生:
                                性別="m";
                                break;
                            case R.id.rabtn_女生:
                                性別="f";
                                break;
                        }

                        地區 = tb尋人地區.getText().toString();


                        new Thread(){
                            @Override
                            public void run() {
                                factory.clear();
                                factory.IfSelect(職業,性別,地區);
                                factory.HTTP(mUI_Handler);


                            }
                        }.start();
                    }

                    catch (Exception e)
                    {
                    }

                    //樓下 自定義 listview的點擊事件
                    listView找人.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //ListView 點擊事件

                            try{

                                factory.MoveTo(position);
                                //在工廠設置移動到現在的陣列位置
                                factory.GetCurrent();
                                //得到目前的位置 (全域變數移動到當下位置)
                                String Uid = factory.GetCurrent().getUid();

                                Bundle bund = new Bundle();

                                bund.putString(CDictionary.CSLECT_UserID,Uid);

                                bund.putString(CDictionary.CSLECT_JOB,職業);

                                Intent intent = new Intent(getActivity(),SlectList.class);

                                intent.putExtras(bund);

                                startActivity(intent);
                                //點選到的UID送過去
                            }
                            catch (Exception e)
                            {

                            }

                        }
                    });
                }
            });
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    private void updateUi() {

        adapter = new SelectListAdapter(getActivity(),factory.ArrayList());
        listView找人.setAdapter(adapter);
        listView找人.invalidate();
    };





    public void inital()
    //初始化
    {
        btn尋人 = getActivity().findViewById(R.id.btn尋人);
        btn尋人.setOnClickListener(btn尋人_click);
        listView找人 = (ListView) getActivity().findViewById(R.id.listview找人);
        imgSelect = getActivity().findViewById(R.id.imgSelect相片);
    }

    Button btn尋人;
    ListView listView找人;
    ImageView imgSelect;

}