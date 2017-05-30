package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.SaleDetailActivity;
import cn.xcom.helper.bean.Front;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.MyImageLoader;

/**
 * Created by zhuchongkun on 16/6/8.
 */
public class SaleAdapter extends RecyclerView.Adapter<SaleAdapter.ViewHolder> {
    private List<Front> addList;
    private Context context;

    public SaleAdapter(List<Front> addList, Context context) {
        this.addList = addList;
        this.context = context;
    }


    @Override
    public SaleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sale_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final Front front = addList.get(position);
        if (front.getPicturelist().size() > 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    front.getPicturelist().get(0).getFile(), viewHolder.imageView);
        } else if (front.getPicturelist().size() == 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, viewHolder.imageView);
        }
        viewHolder.textView1.setText(front.getGoodsname());
        viewHolder.textView2.setText(front.getDescription());
        viewHolder.textView3.setText("￥" + front.getPrice());
        viewHolder.textView4.setText(front.getOprice());
        viewHolder.textView4.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中划线
        viewHolder.textView5.setText("已售" + front.getSellnumber());
        viewHolder.tv_name.setText(front.getUsername());
        viewHolder.tv_comment_count.setText("评价" + front.getCommentCount() + "条");
        //对cinvertview进行监听
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SaleDetailActivity.class);
                intent.putExtra("id", front.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return addList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView1, textView2, textView3, textView4, textView5, tv_name, tv_comment_count;

        public ViewHolder(View convertView) {
            super(convertView);
            imageView = (ImageView) convertView.findViewById(R.id.imgview);
            textView1 = (TextView) convertView.findViewById(R.id.title);
            textView2 = (TextView) convertView.findViewById(R.id.content);
            textView3 = (TextView) convertView.findViewById(R.id.price);
            textView4 = (TextView) convertView.findViewById(R.id.oldprice);
            textView5 = (TextView) convertView.findViewById(R.id.havesell);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_comment_count = (TextView) convertView.findViewById(R.id.commen_count);
        }
    }
}
