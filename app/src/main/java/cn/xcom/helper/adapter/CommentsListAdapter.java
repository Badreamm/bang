package cn.xcom.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.Evaluate;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by Administrator on 2016/10/19 0019.
 */

public class CommentsListAdapter extends BaseAdapter {
    private Context mContext;
    private List<Evaluate> evaluates;

    public CommentsListAdapter(Context context, List<Evaluate> evaluates){
        mContext = context;
        this.evaluates = evaluates;
    }

    @Override
    public int getCount() {
        return evaluates.size();
    }

    @Override
    public Object getItem(int position) {
        return evaluates.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view ;
        ViewHolder holder;
        if(convertView == null){
            view =LayoutInflater.from(mContext).inflate(R.layout.item_comments,parent,false);
            holder = new ViewHolder();
            holder.userImg = (ImageView) view.findViewById(R.id.iv_user_img);
            holder.userNameTv = (TextView) view.findViewById(R.id.tv_user_name);
            holder.contentTv = (TextView) view.findViewById(R.id.tv_content);
            holder.timeTv = (TextView) view.findViewById(R.id.tv_time);
            holder.ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            view.setTag(holder);
        }else{
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        Evaluate evaluate = evaluates.get(position);
        if("".equals(evaluate.getPhoto())){
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.userImg);
        }else{
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                   evaluate.getPhoto(), holder.userImg);
        }
        holder.userNameTv.setText(evaluate.getUsername());
        holder.contentTv.setText(evaluate.getContent());
        holder.timeTv.setText(evaluate.getAdd_time());
        holder.ratingBar.setNumStars(Integer.valueOf(evaluate.getScore()));
        return view;
    }

    class ViewHolder{
        ImageView userImg;
        TextView userNameTv,contentTv,timeTv;
        RatingBar ratingBar;
    }
}
