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
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.xcom.helper.R;
import cn.xcom.helper.activity.BillActivity;
import cn.xcom.helper.activity.MyPostOrderDetailActivity;
import cn.xcom.helper.activity.PaymentActivity;
import cn.xcom.helper.activity.PostCommentActivity;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.order.MyPostOrderFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/14 0014.
 */

public class OrderRecyclerViewAdapter extends RecyclerView.Adapter<OrderRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private List<TaskItemInfo> taskItemInfos;
    private Fragment fragment;
    private int type;

    public OrderRecyclerViewAdapter(Context context, List<TaskItemInfo> taskItemInfos, Fragment fragment, int orderType) {
        mContext = context;
        this.taskItemInfos = taskItemInfos;
        this.fragment = fragment;
        type = orderType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_my_post_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TaskItemInfo taskItemInfo = taskItemInfos.get(position);

        //0未付款 1已付款
        String payState = taskItemInfo.getPaystatus();
        String state = taskItemInfo.getState();
        if (payState.equals("0")) {
            holder.payStateDesTv.setText("支付未托管");
            holder.trusteeshipBt.setVisibility(View.VISIBLE);
        } else if (payState.equals("1")) {
            holder.payStateDesTv.setText("支付已托管");
            holder.trusteeshipBt.setVisibility(View.GONE);
        }


        //0＝》未开始,1＝》已开始，2=》已抢单，3已上门，4申请付款，
        // 5已付款， 6服务者评价，7发布者评价， 10 已完成，－1=》已取消
        if (state.equals("1")) {
            holder.orderCompleteStateTv.setText("未完成");
            holder.orderStateTv.setText("未抢单");
            holder.payBt.setVisibility(View.GONE);
            holder.toCommentBtn.setVisibility(View.GONE);
        } else if (state.equals("-1")) {
            holder.orderCompleteStateTv.setText("未完成");
            holder.orderStateTv.setText("已取消");
            holder.payStateDesTv.setText("已退款");
            holder.trusteeshipBt.setVisibility(View.GONE);
            holder.payBt.setVisibility(View.GONE);
            holder.toCommentBtn.setVisibility(View.GONE);
        } else if (state.equals("2")) {
            holder.orderCompleteStateTv.setText("未完成");
            holder.orderStateTv.setText("已被抢");
            holder.payBt.setVisibility(View.VISIBLE);
            holder.toCommentBtn.setVisibility(View.GONE);
        } else if (state.equals("3") || state.equals("4")) {
            holder.orderCompleteStateTv.setText("未完成");
            holder.orderStateTv.setText("已上门");
            holder.payBt.setVisibility(View.VISIBLE);
            holder.bt_cancel.setVisibility(View.VISIBLE);
            holder.toCommentBtn.setVisibility(View.GONE);
        } else {
            holder.orderCompleteStateTv.setText("已完成");
            holder.orderStateTv.setText("已完成");
            holder.payBt.setVisibility(View.GONE);
            if (taskItemInfo.getEvaluate().size() == 0) {
                holder.toCommentBtn.setVisibility(View.VISIBLE);
            } else {
                holder.toCommentBtn.setVisibility(View.GONE);
            }
        }

        Date date = new Date();
        date.setTime(Long.parseLong(taskItemInfo.getTime()) * 1000);
        holder.publishTimeTv.setText(new SimpleDateFormat("MM-dd  HH:mm:ss").format(date));
        holder.orderNumTv.setText(taskItemInfo.getOrder_num());
        holder.orderTitleTv.setText(taskItemInfo.getTitle());
        holder.phoneTv.setText(taskItemInfo.getPhone());
        if (taskItemInfo.getApply() == null || taskItemInfo.getApply().equals("")) {
            holder.applyNameTv.setText("无人接单");
            holder.applyNameTv.setClickable(false);
        } else {
            if (taskItemInfo.getApply().getName().equals("")) {
                holder.applyNameTv.setText(taskItemInfo.getApply().getPhone());
                holder.applyNameTv.setClickable(true);
            } else {
                holder.applyNameTv.setText(taskItemInfo.getApply().getName());
                holder.applyNameTv.setClickable(true);
            }
        }

        holder.payBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("确认付款").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String orderNum = taskItemInfo.getOrder_num();
                                confirmPayment(orderNum, position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        holder.bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mContext).setTitle("取消订单").setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String orderNum = taskItemInfo.getOrder_num();
                                cancelPayment(orderNum, position);
                            }
                        }).setNegativeButton("取消", null).show();
            }
        });

        holder.toCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PostCommentActivity.class);
                intent.putExtra("order_id", taskItemInfo.getId());
                intent.putExtra("type", "1");//任务是1,商城是2
                fragment.startActivityForResult(intent, MyPostOrderFragment.POST_ORDER_REQUEST_CODE);
            }
        });

        holder.trusteeshipBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PaymentActivity.class);
                intent.putExtra("price", taskItemInfo.getPrice());
                intent.putExtra("tradeNo", taskItemInfo.getOrder_num());
                intent.putExtra("body", "任务费用");
                intent.putExtra("type", "1");
                intent.putExtra("orderType", 1);//1--任务,2--商品
                fragment.startActivityForResult(intent, MyPostOrderFragment.POST_ORDER_REQUEST_CODE);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyPostOrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("task_info", taskItemInfos.get(position));
                bundle.putInt("type", type);
                intent.putExtra("bundle", bundle);
                fragment.startActivityForResult(intent, MyPostOrderFragment.POST_ORDER_REQUEST_CODE);
            }
        });


    }

    @Override
    public int getItemCount() {
        return taskItemInfos.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_trusteeship_payment:

                break;

        }
    }

    private void confirmPayment(String orderNum, final int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_num", orderNum);
        requestParams.put("state", "5");
        HelperAsyncHttpClient.get(NetConstant.UPDATE_TASK_STATE, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(mContext, "确认付款成功", Toast.LENGTH_SHORT).show();
                                    taskItemInfos.remove(position);
                                    notifyDataSetChanged();
                                    BillActivity billActivity = (BillActivity) mContext;
                                    billActivity.changePager();
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

    private void cancelPayment(String orderNum, final int position) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_num", orderNum);
        requestParams.put("state", "-1");
        HelperAsyncHttpClient.get(NetConstant.UPDATE_TASK_STATE, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(mContext, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    taskItemInfos.remove(position);
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


    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderCompleteStateTv, publishTimeTv, orderNumTv,
                orderTitleTv, phoneTv, applyNameTv, orderStateTv, payStateDesTv, bt_cancel;
        private Button payBt, trusteeshipBt, toCommentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            orderCompleteStateTv = (TextView) itemView.findViewById(R.id.tv_order_complete_state);
            publishTimeTv = (TextView) itemView.findViewById(R.id.tv_publish_time);
            orderNumTv = (TextView) itemView.findViewById(R.id.tv_order_num);
            orderTitleTv = (TextView) itemView.findViewById(R.id.tv_order_title);
            phoneTv = (TextView) itemView.findViewById(R.id.tv_phone);
            applyNameTv = (TextView) itemView.findViewById(R.id.tv_apply_name);
            orderStateTv = (TextView) itemView.findViewById(R.id.tv_order_state);
            payStateDesTv = (TextView) itemView.findViewById(R.id.tv_pay_state_des);
            payBt = (Button) itemView.findViewById(R.id.bt_confirm_payment);
            trusteeshipBt = (Button) itemView.findViewById(R.id.bt_trusteeship_payment);
            toCommentBtn = (Button) itemView.findViewById(R.id.bt_to_comment);
            bt_cancel = (TextView) itemView.findViewById(R.id.bt_cancel);
        }
    }


}
