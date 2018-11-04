package com.example.yao.pm.MapFactory;

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

import com.example.yao.pm.ActMain;
import com.example.yao.pm.R;
import com.example.yao.pm.SelectFragment;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;


public class ListAdapter extends BaseAdapter {

    private LayoutInflater myInflater;
    private ArrayList<CListViewMap> cListViewMaps;


    public ListAdapter(Context context, ArrayList<CListViewMap> cListViewMaps){
        try{
            myInflater = LayoutInflater.from(context);
            this.cListViewMaps = cListViewMaps;
        }
        catch (Exception e)
        {

        }
    }

    @Override
    public int getCount() {
        //取得的最大欄位數量 就是陣列的長度
        try{
            return cListViewMaps.size();
        }
            catch (Exception e)
            {

            }
            return 0;
    }

    @Override
    public Object getItem(int position) {
        return cListViewMaps.get(position);

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null)
        {
            convertView = myInflater.inflate(R.layout.item_listview_map,null);
            holder = new ViewHolder();
            holder.lbl標題 = convertView.findViewById(R.id.lbl地標標題);
            holder.lbl內文 = convertView.findViewById(R.id.lbl地標內文);
            holder.img地圖 = convertView.findViewById(R.id.img地圖相片);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        CListViewMap map = cListViewMaps.get(position);

        holder.lbl標題.setText(map.getTitle());

        if(map.getBitmap() != null)
        {
            holder.img地圖.setImageBitmap(map.getBitmap());
        }
          else{
            StrictMode.ThreadPolicy l_policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(l_policy);
            try {
                URL url = new URL("http://link.photo.pchome.com.tw/imgdata/czo0NDoiWlpMUzh1bENjaEEvTmJzS2dpY2ZWNlBWZUNpai9NeFppaVdnNEhqZStPbz0iOw==.jpg");
                URLConnection con = url.openConnection();
                InputStream streamIn = con.getInputStream();
                Bitmap pic = BitmapFactory.decodeStream(streamIn);
                holder.img地圖.setImageBitmap(pic);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        holder.lbl內文.setText(map.getContent());
        return convertView;
    }
    private class ViewHolder {
        TextView lbl標題;
        ImageView img地圖;
        TextView lbl內文;
    }
}
