package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.activity.SpaceVideoDetialActivity;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class ListGridviews extends BaseAdapter{
    private List <String>list;
    private Context context;


    public ListGridviews(Context context, List <String>list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return  list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_gridview,null);
            viewHolder.imageView1= (ImageView) convertView.findViewById(R.id.add_image);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }


        MyImageLoader.display(list.get(position),viewHolder.imageView1);
//            viewHolder.imageView1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    if (list.get(position)== NetConstant.NET_DISPLAY_IMG||list.get(position)==null){
//                        Toast.makeText(context,"暂无图片",Toast.LENGTH_SHORT).show();
//                        Log.d("=====null", list.get(position));
//                    }else{
//                        Log.d("=====ninini",list.get(position));
//                        Intent intent = new Intent(context, SpaceImageDetailActivity.class);
//                        Bundle bundle=new Bundle();
//                        bundle.putSerializable("list111", (Serializable) list);
//                        intent.putExtra("position",position);
//                        intent.putExtras(bundle);
//                        int[] location = new int[2];
//                        intent.putExtra("locationX", location[1]);//必须
//                        intent.putExtra("locationY", location[0]);//必须
//                        intent.putExtra("width",viewHolder.imageView1.getWidth());//必须
//                        intent.putExtra("height", viewHolder.imageView1.getHeight());//必须
//                        context.startActivity(intent);
//                    }
//
//                  }
//
//

//        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView1,imageView2;
    }
}
