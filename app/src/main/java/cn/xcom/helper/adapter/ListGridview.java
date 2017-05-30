package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import cn.finalteam.toolsfinal.io.stream.ByteArrayOutputStream;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.activity.SpaceVideoDetialActivity;
import cn.xcom.helper.bean.Convenience;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.BgImageViewAware;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.ToastUtil;

import static cn.finalteam.toolsfinal.io.IOUtils.copy;

/**
 * Created by Administrator on 2016/9/28 0028.
 */
public class ListGridview extends BaseAdapter{
    private List <String>list;
    private Context context;
    private List<Convenience>convenienceList;
    private int pposition;
    Drawable drawable;
    ImageView img;


    public ListGridview(Context context, List <String>list,List<Convenience> convenienceList,int pposition) {
        this.context = context;
        this.list = list;
        this.convenienceList = convenienceList;
        this.pposition = pposition;
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
        Convenience convenience = convenienceList.get(pposition);
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_gridview,null);
            viewHolder.imageView1= (ImageView) convertView.findViewById(R.id.add_image);
            img = viewHolder.imageView1;
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        if (!convenience.getVideo().equals("")) {
            if (position == convenience.getPic().size() - 1) {
                img.setImageResource(R.drawable.ic_bofang);
                img.setScaleType(ImageView.ScaleType.FIT_CENTER);
                MyImageLoader.displayBg(list.get(position),viewHolder.imageView1);

            }else {
                MyImageLoader.display(list.get(position),viewHolder.imageView1);

            }
        }else {
            MyImageLoader.display(list.get(position),viewHolder.imageView1);
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView1,imageView2;
    }
}
