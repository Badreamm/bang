package cn.xcom.helper.adapter;

import android.content.Context;
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
 * Created by 尉鑫鑫 on 2016/9/20.
 */
public class GridViewAdapter extends BaseAdapter {
    private ArrayList<PhotoInfo> list;
    private Context context;


    public GridViewAdapter(Context context, ArrayList<PhotoInfo> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // return list.size();
        return list.size() > 9 ? 9 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_gridview, null);
            viewHolder.imageView1 = (ImageView) convertView.findViewById(R.id.add_image);
            //viewHolder.imageView2 = (ImageView) convertView.findViewById(R.id.delete_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(list.size()>0){
            MyImageLoader.display("file:/" + list.get(position).getPhotoPath(), viewHolder.imageView1);
            /*Log.d("======path", list.get(position).getPhotoPath() + "");
            viewHolder.imageView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context).setTitle("删除图片").setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    list.remove(position);
                                    notifyDataSetChanged();
                                }
                            }).setNegativeButton("取消", null).show();
                }
            });*/
        }


        return convertView;
    }

    public class ViewHolder {
        ImageView imageView1, imageView2;
    }
}
