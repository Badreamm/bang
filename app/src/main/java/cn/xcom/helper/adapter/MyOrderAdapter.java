package cn.xcom.helper.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.activity.MyOrderDetailActivity;
import cn.xcom.helper.activity.PaymentActivity;
import cn.xcom.helper.activity.PostCommentActivity;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.order.MyOrderFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.MyImageLoader;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单页面适配器
 * 卖家订单页面适配器
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHold> {
    private Context mContext;
    private List<ShopGoodInfo> goodInfos;
    private UserInfo userInfo;
    private Fragment fragment;
    private int orderType;

    public MyOrderAdapter(Context context, List<ShopGoodInfo> goodInfos,
                          Fragment fragment, int orderType) {
        mContext = context;
        this.goodInfos = goodInfos;
        userInfo = new UserInfo(mContext);
        this.fragment = fragment;
        this.orderType = orderType;
    }

    @Override
    public ViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_order, parent, false);
        return new ViewHold(view);

    }

    @Override
    public int getItemCount() {
        return goodInfos.size();
    }

    @Override
    public void onBindViewHolder(ViewHold holder, final int position) {
        final ShopGoodInfo goodInfo = goodInfos.get(position);

        if (goodInfo.getPicture().size() > 0) {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                    goodInfo.getPicture().get(0).getFile(), holder.goodImage);
        } else {
            MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, holder.goodImage);
        }
        holder.goodTitle.setText(goodInfo.getGoodsname());
        holder.goodPrice.setText(goodInfo.getMoney());

        if (orderType == OrderHelper.BuyerOrder) {
            switch (Integer.valueOf(goodInfo.getState())) {
                case OrderHelper.CANCELED:
                    holder.orderState.setText("已取消");
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.GONE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNPAY:
                    holder.orderState.setText("待付款");
                    holder.payBtn.setVisibility(View.VISIBLE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.GONE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
                case OrderHelper.UN_SEND_OUT:
                    holder.orderState.setText("待发货");
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.VISIBLE);
                    holder.commentBtn.setVisibility(View.GONE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNCONFIRMED:
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.GONE);
                    if("送货上门".equals(goodInfo.getDelivery())){
                        holder.confirmBtn.setVisibility(View.VISIBLE);
                        holder.orderState.setText("待确认");
                    }else{
                        holder.confirmBtn.setVisibility(View.GONE);
                        holder.orderState.setText("待消费");
                    }
                    break;
                case OrderHelper.BUYER_UNCOMMENT:
                    holder.orderState.setText("待评价");
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.VISIBLE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
                case OrderHelper.SELLER_UNCOMMENT:
                    holder.orderState.setText("已评价");
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.GONE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
                case OrderHelper.COMPLETED:
                    holder.orderState.setText("已完成");
                    holder.payBtn.setVisibility(View.GONE);
                    holder.cancelPaymentBtn.setVisibility(View.GONE);
                    holder.commentBtn.setVisibility(View.GONE);
                    holder.confirmBtn.setVisibility(View.GONE);
                    break;
            }

        } else { //orderType == OrderHelper.SellerOrder 卖家
            holder.payBtn.setVisibility(View.GONE);
            holder.cancelPaymentBtn.setVisibility(View.GONE);
            holder.commentBtn.setVisibility(View.GONE);
            holder.confirmBtn.setVisibility(View.GONE);
            switch (Integer.valueOf(goodInfo.getState())) {
                case OrderHelper.CANCELED:
                    holder.orderState.setText("已取消");
                    break;
                case OrderHelper.UNPAY:
                    holder.orderState.setText("待付款");
                    break;
                case OrderHelper.UN_SEND_OUT:
                    holder.orderState.setText("待发货");
                    break;
                case OrderHelper.UNCONFIRMED:
                    if("送货上门".equals(goodInfo.getDelivery())){
                        holder.orderState.setText("待确认");
                    }else{
                        holder.orderState.setText("待消费");
                    }
                    break;
                case OrderHelper.BUYER_UNCOMMENT:
                    holder.orderState.setText("待评价");
                    break;
                case OrderHelper.SELLER_UNCOMMENT:
                    holder.orderState.setText("已评价");
                    break;
                case OrderHelper.COMPLETED:
                    holder.orderState.setText("已完成");
            }

        }


        holder.payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperApplication.getInstance().type="";
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra("price",goodInfo.getMoney());
                intent.putExtra("tradeNo",goodInfo.getOrder_num());
                intent.putExtra("body",goodInfo.getGoodsname());
                intent.putExtra("type","2");//1--任务,2--商品
                fragment.startActivityForResult(intent, MyOrderFragment.MY_ORDER_REQUEST);
            }
        });

        holder.cancelPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("取消订单").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                cancelOrde(goodInfo.getOrder_num(), position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("order_id", goodInfo.getId());
                intent.putExtra("type", "2");//任务是1,商城是2
                fragment.startActivityForResult(intent, MyOrderFragment.MY_ORDER_REQUEST);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyOrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("good", goodInfo);
                intent.putExtra("bundle", bundle);
                intent.putExtra("order_type", orderType);
                fragment.startActivityForResult(intent, MyOrderFragment.MY_ORDER_REQUEST);
            }
        });

        holder.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("确认收货").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmOrder(goodInfo.getOrder_num(),position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

    }

    class ViewHold extends RecyclerView.ViewHolder {
        private ImageView goodImage;
        private TextView goodPrice, goodTitle, orderState;
        private Button payBtn, commentBtn, cancelPaymentBtn,confirmBtn;

        public ViewHold(View itemView) {
            super(itemView);
            goodImage = (ImageView) itemView.findViewById(R.id.iv_order_image);
            goodTitle = (TextView) itemView.findViewById(R.id.tv_order_title);
            orderState = (TextView) itemView.findViewById(R.id.tv_order_state);
            goodPrice = (TextView) itemView.findViewById(R.id.tv_money);
            payBtn = (Button) itemView.findViewById(R.id.btn_to_pay);
            commentBtn = (Button) itemView.findViewById(R.id.btn_to_comment);
            cancelPaymentBtn = (Button) itemView.findViewById(R.id.btn_cancel_payment);
            confirmBtn = (Button) itemView.findViewById(R.id.btn_confirm);
        }
    }

    private void cancelOrde(String orderNum, final int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("ordernum", orderNum);
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.CANCEL_ORDER, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(mContext, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    goodInfos.remove(position);
                                    notifyDataSetChanged();
                                } else {
                                    String data = response.getString("data");
                                    Toast.makeText(mContext, data, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });

    }

    private void confirmOrder(String orderNum, final int position){
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_num", orderNum);
        requestParams.put("state", OrderHelper.BUYER_UNCOMMENT);
        HelperAsyncHttpClient.get(NetConstant.UPDATE_SHOPPING_STATE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(mContext, "确认收货成功", Toast.LENGTH_SHORT).show();
                            goodInfos.remove(position);
                            notifyDataSetChanged();
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(mContext,data, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
