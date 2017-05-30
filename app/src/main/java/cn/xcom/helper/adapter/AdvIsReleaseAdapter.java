package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.AdvertisingEditActivity;
import cn.xcom.helper.activity.PaymentActivity;
import cn.xcom.helper.activity.ReleaseAdvertisingActivity;
import cn.xcom.helper.bean.AdvReleaseBean;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AdvIsReleaseAdapter extends RecyclerView.Adapter<AdvIsReleaseAdapter.ViewHolder> {
    private List<AdvReleaseBean> list;
    private Context context;
    private UserInfo userInfo;
    private String payId;

    public AdvIsReleaseAdapter(List<AdvReleaseBean> list, Context context) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);


    }

    @Override
    public AdvIsReleaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adv_no_release_item, parent, false);
        return new AdvIsReleaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvIsReleaseAdapter.ViewHolder holder, final int position) {
        final AdvReleaseBean releaseBean = list.get(position);
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + releaseBean.getPhoto(), holder.adv_photo);
        holder.advName.setText(releaseBean.getRealname());
        holder.advTime.setText(TimeUtils.getDateToString(Long.parseLong(releaseBean.getCreate_time()) * 1000));
        holder.advStart.setText(DateUtil.getDateToString(Long.parseLong(releaseBean.getBegintime())*1000));
        holder.advEnd.setText(DateUtil.getDateToString(Long.parseLong(releaseBean.getEndtime())*1000));
        holder.advGopay.setVisibility(View.GONE);
        holder.advNetAddress.setText(releaseBean.getSlide_url());
        holder.advPrice.setText(releaseBean.getPrice());
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + releaseBean.getSlide_pic(), holder.advImg);
        holder.advEdit.setVisibility(View.VISIBLE);
        holder.advEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AdvertisingEditActivity.class);
                intent.putExtra("id", releaseBean.getSlide_id());
                intent.putExtra("img",releaseBean.getSlide_pic());
                context.startActivity(intent);
            }
        });
        holder.advShanchu.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoundImageView adv_photo;
        private TextView advName, advTime, advGopay, advEdit, advNetAddress, advPrice, advStart, advEnd;
        private ImageView advShanchu, advImg;

        public ViewHolder(View itemView) {
            super(itemView);
            adv_photo = (RoundImageView) itemView.findViewById(R.id.adv_convenience_photo);
            advName = (TextView) itemView.findViewById(R.id.adv_convenience_name);
            advTime = (TextView) itemView.findViewById(R.id.adv_convenience_time);
            advGopay = (TextView) itemView.findViewById(R.id.adv_go_pay);
            advEdit = (TextView) itemView.findViewById(R.id.adv_edit);
            advNetAddress = (TextView) itemView.findViewById(R.id.adv_release_net_address);
            advPrice = (TextView) itemView.findViewById(R.id.price);
            advStart = (TextView) itemView.findViewById(R.id.release_start_time);
            advEnd = (TextView) itemView.findViewById(R.id.release_end_time);
            advShanchu = (ImageView) itemView.findViewById(R.id.frg_adv_shanchu);
            advImg = (ImageView) itemView.findViewById(R.id.adv_release_img);
        }
    }

}
