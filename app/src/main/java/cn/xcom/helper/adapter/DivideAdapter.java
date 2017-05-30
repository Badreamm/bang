package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.ReleaseActivity;
import cn.xcom.helper.bean.DictionaryList;


/**
 * Created by 尉鑫鑫 on 2016/9/20.
 */
public class DivideAdapter extends BaseAdapter {
    private Context context;
    private List<DictionaryList> list;

    public DivideAdapter(Context context, List<DictionaryList> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        ViewHolder holder;
        if (convertView==null) {
            holder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.divider_layout, null);
            holder.groupItem=(TextView) convertView.findViewById(R.id.groupItem);
            convertView.setTag(holder);

        }
        else{
            holder=(ViewHolder) convertView.getTag();
        }
        final DictionaryList dictionaryList=list.get(position);
        holder.groupItem.setTextColor(Color.BLACK);
        holder.groupItem.setText(dictionaryList.getName());

        return convertView;

    }
    static class ViewHolder {
        TextView groupItem;
    }
}
