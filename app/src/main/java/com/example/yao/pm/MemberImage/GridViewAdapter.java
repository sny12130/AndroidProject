package com.example.yao.pm.MemberImage;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.yao.pm.R;
import java.util.List;

public class GridViewAdapter  extends BaseAdapter {


    private Context context;

    private List<CImage> ImgList;



    public GridViewAdapter(Context context,  List<CImage> ImgList) {
         super();
         this.context = context;
         this.ImgList = ImgList;
    }


    @Override
    public int getCount() {
        return  ImgList.size();
    }

    @Override
    public Object getItem(int position) {
        return ImgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview_member, null);
            holder = new ViewHolder();
        
            holder.img會員相簿展示 = convertView.findViewById(R.id.gridview_img會員相簿展示);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        CImage current = ImgList.get(position);

        holder.img會員相簿展示.setImageBitmap(current.getBitmap());

        holder.imageView = new ImageView(context);

        holder.imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));

        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        holder.imageView.setPadding(8, 8, 8, 8);

        return convertView;

    }

    public class ViewHolder {
       ImageView img會員相簿展示;
       ImageView imageView;
    }
}