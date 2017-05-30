package cn.xcom.helper.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.xcom.helper.R;
import cn.xcom.helper.utils.MyImageLoader;


/**
 * Created by Administrator on 2016/5/27.
 */
public class LocalImgGridAdapter extends BaseAdapter{

    private int i;
    private LayoutInflater mLayoutInflater;
    private ArrayList<PhotoInfo> mWorkImgs;
    private Context mContext;
    public LocalImgGridAdapter(ArrayList<PhotoInfo> workImgs, Context context) {
        this.mContext = context;
        this.mWorkImgs = workImgs;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.i = mWorkImgs.size();
    }

    @Override
    public int getCount() {
        return i > 8 ? 9 : i;
    }

    @Override
    public Object getItem(int position) {
        return i;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyGridViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new MyGridViewHolder();
            convertView = mLayoutInflater.inflate(R.layout.user_img_item, parent, false);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.iv_user_img);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MyGridViewHolder) convertView.getTag();
        }
        MyImageLoader.display("file:/" + mWorkImgs.get(position).getPhotoPath(), viewHolder.imageView);
        return convertView;
    }

    private static class MyGridViewHolder {
        ImageView imageView;
    }
}
