package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.OybGoodDetailActivity;
import cn.xcom.helper.bean.OybGood;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.onyuanbuy.OybGoodsFragment;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.view.AlwaysMarqueeTextView;

/**
 * Created by hzh on 2017/7/8.
 */

public class OybGoodAdapter extends RecyclerView.Adapter<OybGoodAdapter.ViewHold> {
    private Context context;
    private List<OybGood> goods;
    private int type;
    public List<ViewHold> myViewHoldList = new ArrayList<>();

    public OybGoodAdapter(Context context, List<OybGood> goods, int type) {
        this.context = context;
        this.goods = goods;
        this.type = type;
    }


    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_oyb_good, parent, false);
        return new ViewHold(view);
    }

    @Override
    public void onBindViewHolder(ViewHold holder, int position) {
        holder.setDataPosition(position);
        if(!myViewHoldList.contains(holder)){
            myViewHoldList.add(holder);
        }
        final OybGood good = goods.get(position);
        holder.titleTv.setText(good.getTitle());
        if (good.getSmeta() != null && good.getSmeta().size() > 0) {
            MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY +
                    good.getSmeta().get(0).getUrl(), holder.imageView);
        } else {
            MyImageLoader.display(NetConstant.NEW_IMG_DISPLAY, holder.imageView);
        }
        if (type == OybGoodsFragment.ALL_GOODS) {
            holder.progressTag.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.waitFlag.setVisibility(View.GONE);
            holder.annoFlag.setVisibility(View.GONE);
            if (!good.getCount().equals("0")) {
                int count = Integer.valueOf(good.getCount());
                int all = Integer.valueOf(good.getPrice());
                DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
                //可以设置精确几位小数
                df.setMaximumFractionDigits(1);
                //模式 例如四舍五入
                df.setRoundingMode(RoundingMode.HALF_UP);
                double accuracy_num = count / Double.valueOf(all) * 100;
                String result = df.format(accuracy_num);
                holder.progressTv.setText(result + "%");
                holder.progressBar.setProgress((int) accuracy_num);
            }
        } else if (type == OybGoodsFragment.WAIT_GOODS) {
            holder.progressTag.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.waitFlag.setVisibility(View.VISIBLE);
            holder.annoFlag.setVisibility(View.GONE);
        } else if (type == OybGoodsFragment.ANNO_GOODS) {
            holder.progressTag.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.waitFlag.setVisibility(View.GONE);
            holder.annoFlag.setVisibility(View.VISIBLE);

            holder.winneTv.setText("获奖用户：" + good.getName());
            holder.lunckNumTv.setText("幸运数字：" + good.getNum());
            holder.countTv.setText("参与次数：" + good.getCount());
            holder.annoTimeTv.setText("揭晓时间：" + TimeUtils.newChangeTime(good.getTime()));
        }

        if (type != OybGoodsFragment.WAIT_GOODS) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, OybGoodDetailActivity.class);
                    intent.putExtra("mark", good.getMark());
                    context.startActivity(intent);
                }
            });
        }

    }

    public void notifyData(){
        for (int i = 0;i<myViewHoldList.size();i++){
            if(type == OybGoodsFragment.WAIT_GOODS){
                myViewHoldList.get(i).countTimeTv.setText(goods.get(myViewHoldList.get(i).position).getShowTime());
            }
        }
    }


    @Override
    public int getItemCount() {
        return goods.size();
    }

    class ViewHold extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTv;
        private TextView progressTv, winneTv, countTv, lunckNumTv, countTimeTv;
        private ProgressBar progressBar;
        private LinearLayout progressTag, waitFlag, annoFlag;
        private AlwaysMarqueeTextView annoTimeTv;
        private int position;
        public ViewHold(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            titleTv = (TextView) itemView.findViewById(R.id.title);
            progressTv = (TextView) itemView.findViewById(R.id.progress);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            progressTag = (LinearLayout) itemView.findViewById(R.id.progress_tag);
            waitFlag = (LinearLayout) itemView.findViewById(R.id.wait_flag);
            annoFlag = (LinearLayout) itemView.findViewById(R.id.anno_flag);
            winneTv = (TextView) itemView.findViewById(R.id.winner_tv);
            countTv = (TextView) itemView.findViewById(R.id.count_tv);
            countTimeTv = (TextView) itemView.findViewById(R.id.count_time_tv);
            annoTimeTv = (AlwaysMarqueeTextView) itemView.findViewById(R.id.anno_time_tv);
            lunckNumTv = (TextView) itemView.findViewById(R.id.lucky_num_tv);
        }

        private void setDataPosition(int position){
            this.position = position;
        }
    }

}
