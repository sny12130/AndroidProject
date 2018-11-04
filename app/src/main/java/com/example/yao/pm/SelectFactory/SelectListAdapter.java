package com.example.yao.pm.SelectFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yao.pm.MapFactory.CListViewMap;
import com.example.yao.pm.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class SelectListAdapter extends BaseAdapter {

    private LayoutInflater myInflater;

    private ArrayList<CListViewSelect>cListViewSelects;

    public SelectListAdapter(Context context , ArrayList<CListViewSelect>cListViewSelects){

        try{
            myInflater = LayoutInflater.from(context);

            this.cListViewSelects = cListViewSelects;
        }
        catch (Exception e)
        {

        }

    }



    @Override
    public int getCount() {
        return cListViewSelects.size();
    }

    @Override
    public Object getItem(int position) {
        return cListViewSelects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

      SelectListAdapter.ViewHolder holder = null;

        if(convertView == null)
        {
            convertView = myInflater.inflate(R.layout.item_listview_select,null);
            holder = new SelectListAdapter.ViewHolder();
            holder. lbl尋找姓名 = convertView.findViewById(R.id.lbl尋找姓名);
            holder.lbl尋找地區 = convertView.findViewById(R.id.lbl尋找地區);
            holder.img尋找相片 = convertView.findViewById(R.id.imgSelect相片);
            convertView.setTag(holder);
        }
        else{
            holder = (SelectListAdapter.ViewHolder) convertView.getTag();
        }

        CListViewSelect select = cListViewSelects.get(position);

        holder.lbl尋找地區.setText(select.getWorklocation());
        holder.lbl尋找姓名.setText(select.getName());

        if(select.getBitmap() != null)
        {
            holder.img尋找相片.setImageBitmap(select.getBitmap());
        }

           else if(select.getBitmap() == null){

            try {
                StrictMode.ThreadPolicy l_policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(l_policy);
                URL url = new URL("http://link.photo.pchome.com.tw/imgdata/czo0NDoiWlpMUzh1bENjaEEvTmJzS2dpY2ZWNlBWZUNpai9NeFppaVdnNEhqZStPbz0iOw==.jpg");
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(streamIn);
                holder.img尋找相片.setImageBitmap(pic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }



    private class ViewHolder {
        ImageView img尋找相片;
        TextView lbl尋找姓名;
        TextView lbl尋找地區;
    }
}
