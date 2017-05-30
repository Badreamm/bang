package cn.xcom.helper.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.xcom.helper.R;

/**
 * Created by zhuchongkun on 16/6/16.
 */
public class SignCouponAdapter extends RecyclerView.Adapter<SignCouponAdapter.ViewHolder>{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sign_coupon,parent,false);
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
        private TextView tv_type,tv_time,tv_integral,tv_money,tv_introduction;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_type= (TextView) itemView.findViewById(R.id.tv_item_sign_coupon_type);
            tv_time= (TextView) itemView.findViewById(R.id.tv_item_sign_coupon_time);
            tv_money= (TextView) itemView.findViewById(R.id.tv_item_sign_coupon_money);
            tv_introduction= (TextView) itemView.findViewById(R.id.tv_item_sign_coupon_introduction);
            tv_integral=(TextView) itemView.findViewById(R.id.tv_item_sign_coupon_integral);
        }
    }
}
