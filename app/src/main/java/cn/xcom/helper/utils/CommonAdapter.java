package cn.xcom.helper.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;

/**
 * Created by Administrator on 2016/8/12 0012.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflatar;
    private int layoutId;
    public int selectIndex = 0;
    public boolean canselect;

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflatar = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        TextView textView;
        if (canselect) {
            textView = (TextView) holder.getmConvertView().findViewById(R.id.tv_item_help_me_skill_tag);
            if (selectIndex == position) {
                holder.getmConvertView().setBackgroundResource(R.drawable.tv_tag_select);
                holder.getmConvertView().setClickable(true);
                textView.setTextColor(mContext.getResources().getColor(R.color.colorTextWhite));
                notifyDataSetChanged();
            } else {
                holder.getmConvertView().setBackgroundResource(R.drawable.tv_tag);
                holder.getmConvertView().setClickable(false);
                textView.setTextColor(mContext.getResources().getColor(R.color.colorTheme));
                notifyDataSetChanged();
            }
        } else {

        }
        return holder.getmConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);
}
