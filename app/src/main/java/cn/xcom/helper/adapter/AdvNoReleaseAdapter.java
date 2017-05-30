package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.AdvertisingEditActivity;
import cn.xcom.helper.activity.PaymentActivity;
import cn.xcom.helper.activity.SpaceImageDetailActivity;
import cn.xcom.helper.activity.SpaceVideoDetialActivity;
import cn.xcom.helper.bean.AdvReleaseBean;
import cn.xcom.helper.bean.MyMessage;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.record.SoundView;
import cn.xcom.helper.utils.DateUtil;
import cn.xcom.helper.utils.MyImageLoader;
import cn.xcom.helper.utils.NoScrollGridView;
import cn.xcom.helper.utils.RoundImageView;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.TimeUtils;
import cn.xcom.helper.utils.ToastUtil;
import cn.xcom.helper.view.TextViewExpandableAnimation;

/**
 * Created by Administrator on 2017/4/12 0012.
 */

public class AdvNoReleaseAdapter extends RecyclerView.Adapter<AdvNoReleaseAdapter.ViewHolder> {
    private List<AdvReleaseBean> list;
    private Context context;
    private UserInfo userInfo;
    private String payId;

    public AdvNoReleaseAdapter(List<AdvReleaseBean> list, Context context) {
        this.list = list;
        this.context = context;
        userInfo = new UserInfo(context);
        userInfo.readData(context);


    }

    @Override
    public AdvNoReleaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adv_no_release_item, parent, false);
        return new AdvNoReleaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdvNoReleaseAdapter.ViewHolder holder, final int position) {
        final AdvReleaseBean releaseBean = list.get(position);
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + releaseBean.getPhoto(), holder.adv_photo);
        holder.advName.setText(releaseBean.getRealname());

        holder.advGopay.setVisibility(View.VISIBLE);
        holder.advNetAddress.setText(releaseBean.getSlide_url());
        holder.advTime.setText(TimeUtils.getDateToString(Long.parseLong(releaseBean.getCreate_time()) * 1000));
        holder.advStart.setText(DateUtil.getDateToString(Long.parseLong(releaseBean.getBegintime())*1000));
        holder.advEnd.setText(DateUtil.getDateToString(Long.parseLong(releaseBean.getEndtime())*1000));
        holder.advPrice.setText(releaseBean.getPrice());
        MyImageLoader.display(NetConstant.NET_DISPLAY_IMG + releaseBean.getSlide_pic(), holder.advImg);
        holder.advEdit.setVisibility(View.VISIBLE);
        holder.advShanchu.setVisibility(View.VISIBLE);
        holder.advGopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payIntent = new Intent(context, PaymentActivity.class);
                payIntent.putExtra("paytype", "AdvPay");
                payIntent.putExtra("tradeNo", releaseBean.getOrder_num());
                payIntent.putExtra("body", "广告发布");
                payIntent.putExtra("price", releaseBean.getPrice());
                payIntent.putExtra("type", "5");
                context.startActivity(payIntent);

            }
        });
        holder.advEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdvertisingEditActivity.class);
                intent.putExtra("id", releaseBean.getSlide_id());
                intent.putExtra("img", releaseBean.getSlide_pic());
                intent.putExtra("url", releaseBean.getSlide_url());
//                intent.putExtra("theme", releaseBean.());
                context.startActivity(intent);
            }
        });
        holder.advShanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertView mAlertView = new AlertView("提示", "你确定删除自己的广告？", "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, final int position) {
                        if (position == 0) {
                            String url = NetConstant.DELETE_MY_AD;
                            StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(s);
                                        String status = jsonObject.getString("status");
                                        if (status.equals("success")) {
                                            ToastUtil.showShort(context, "删除成功");
                                            list.remove(position);
                                            notifyDataSetChanged();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    ToastUtil.Toast(context, "网络错误，请检查");
                                }
                            });
                            request.putValue("userid", userInfo.getUserId());
                            request.putValue("id", releaseBean.getSlide_id());
                            SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
                        }
                    }
                }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(Object o) {

                    }
                });
                mAlertView.show();
            }
        });

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

    private void getMessagePrice() {
        String url = NetConstant.GET_MESSAGE_PRICE;
        StringPostRequest request = new StringPostRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d("---price", s);
                try {
                    JSONObject object = new JSONObject(s);
                    if (object.optString("status").equals("success")) {

//                        JSONObject publishresult = object.getJSONObject("data");

                        Intent payIntent = new Intent(context, PaymentActivity.class);
                        payIntent.putExtra("paytype", "ConveniencePay");
                        payIntent.putExtra("tradeNo", "M" + payId);
                        payIntent.putExtra("body", "便民圈");
                        payIntent.putExtra("price", object.optString("data"));
                        payIntent.putExtra("type", "4");
//                        payIntent.putExtra("payId",payId);
                        context.startActivity(payIntent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(context, "网络错误，检查您的网络", Toast.LENGTH_SHORT).show();
            }
        });

        request.putValue("userid", userInfo.getUserId());
        SingleVolleyRequest.getInstance(context).addToRequestQueue(request);
    }
}
