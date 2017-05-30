package cn.xcom.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.xcom.helper.R;
import cn.xcom.helper.view.CircleImageView;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name, tv_time, tv_content;
        private CircleImageView iv_head;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_item_message_name);
            tv_time = (TextView) itemView.findViewById(R.id.tv_item_message_time);
            tv_content = (TextView) itemView.findViewById(R.id.tv_item_message_content);
            iv_head= (CircleImageView) itemView.findViewById(R.id.iv_item_message_head);
        }
    }
}
