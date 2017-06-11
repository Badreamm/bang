package cn.xcom.helper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xcom.helper.R;
import cn.xcom.helper.fragment.packet.PacketRecordFragment;

/**
 * Created by hzh on 2017/6/4.
 */

public class PacketRecordAdapter extends RecyclerView.Adapter <PacketRecordAdapter.ViewHolder>{
    private Context mContext;
    private int recordType;

    public PacketRecordAdapter(Context context,int type){
        mContext = context;
        recordType = type;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_packet_record,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView timeTv,nameTv,flagTv,moneyTv;
        public ViewHolder(View itemView) {
            super(itemView);
            timeTv = (TextView) itemView.findViewById(R.id.tv_time);
            nameTv = (TextView) itemView.findViewById(R.id.tv_name);
            flagTv = (TextView) itemView.findViewById(R.id.tv_flag);
            moneyTv = (TextView) itemView.findViewById(R.id.tv_money);
        }
    }
}
