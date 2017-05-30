package cn.xcom.helper.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.OrderHelper;
import cn.xcom.helper.bean.ShopGoodInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/18 0018.
 * 我的订单 商家订单 详情页
 */

public class MyOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private static final int ORDER_DETAIL_REQUEST_CODE = 1122;
    private static final int CANCEL_SUCCESS = 101;
    private static final int PAY_SUCCESS = 113;
    private static final int COMMENT_SUCCESS = 112;

    private ShopGoodInfo goodInfo;
    private TextView userNameTv, mobileTv, goodNameTv, priceTv, goodsCountTv, moneyTv, deliveryTv,
            remarksTv, addressTv, toPaytv, cancelPaymentTv, trackingTv, commentTv, sendOutTv,timeTv;
    private View backView;
    private UserInfo userInfo;
    private int orderType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_detail);
        goodInfo = (ShopGoodInfo) getIntent().getBundleExtra("bundle").getSerializable("good");
        orderType = getIntent().getIntExtra("order_type", OrderHelper.SellerOrder);
        userInfo = new UserInfo(this);
        initView();
        setView();
    }

    private void initView() {
        backView = findViewById(R.id.rl_help_me_back);
        backView.setOnClickListener(this);
        userNameTv = (TextView) findViewById(R.id.tv_username);
        mobileTv = (TextView) findViewById(R.id.tv_mobile);
        goodNameTv = (TextView) findViewById(R.id.tv_good_name);
        priceTv = (TextView) findViewById(R.id.tv_price);
        goodsCountTv = (TextView) findViewById(R.id.tv_goods_count);
        moneyTv = (TextView) findViewById(R.id.tv_money);
        deliveryTv = (TextView) findViewById(R.id.tv_delivery);
        remarksTv = (TextView) findViewById(R.id.tv_remarks);
        addressTv = (TextView) findViewById(R.id.tv_address);
        toPaytv = (TextView) findViewById(R.id.tv_to_pay);
        toPaytv.setOnClickListener(this);
        cancelPaymentTv = (TextView) findViewById(R.id.tv_cancel_payment);
        cancelPaymentTv.setOnClickListener(this);
        trackingTv = (TextView) findViewById(R.id.tv_tracking);
        commentTv = (TextView) findViewById(R.id.tv_comment);
        commentTv.setOnClickListener(this);
        sendOutTv = (TextView) findViewById(R.id.tv_send_out);
        sendOutTv.setOnClickListener(this);
        timeTv = (TextView) findViewById(R.id.tv_time);
    }

    private void setView() {
        userNameTv.setText(goodInfo.getUsername());
        mobileTv.setText(goodInfo.getMobile());
        goodNameTv.setText(goodInfo.getGoodsname());
        priceTv.setText("￥" + goodInfo.getPrice());
        goodsCountTv.setText(goodInfo.getNumber());
        moneyTv.setText(goodInfo.getMoney());
        deliveryTv.setText(goodInfo.getDelivery());
        remarksTv.setText(goodInfo.getRemarks());
        addressTv.setText(goodInfo.getAddress());
        trackingTv.setText("卷码:" + goodInfo.getTracking());
        String state = goodInfo.getState();
        //状态：-1已取消，1:未付款，2未发货，3未确认，4买家未评价，5卖家未评价，10订单完成

        if (orderType == OrderHelper.BuyerOrder) {
            switch (Integer.valueOf(state)) {
                case OrderHelper.CANCELED:
                    trackingTv.setVisibility(View.GONE);//卷码未支付时隐藏
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNPAY:
                    trackingTv.setVisibility(View.GONE);
                    toPaytv.setVisibility(View.VISIBLE);
                    cancelPaymentTv.setVisibility(View.VISIBLE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UN_SEND_OUT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.UNCONFIRMED:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.BUYER_UNCOMMENT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.VISIBLE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.SELLER_UNCOMMENT:
                    trackingTv.setVisibility(View.VISIBLE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
                case OrderHelper.COMPLETED:
                    trackingTv.setVisibility(View.GONE);
                    toPaytv.setVisibility(View.GONE);
                    cancelPaymentTv.setVisibility(View.GONE);
                    commentTv.setVisibility(View.GONE);
                    sendOutTv.setVisibility(View.GONE);
                    break;
            }

        } else {
            trackingTv.setVisibility(View.GONE);
            toPaytv.setVisibility(View.GONE);
            cancelPaymentTv.setVisibility(View.GONE);
            commentTv.setVisibility(View.GONE);
            if (Integer.valueOf(goodInfo.getState())== OrderHelper.UN_SEND_OUT && !goodInfo.getDelivery().equals("同城自取")) {
                sendOutTv.setVisibility(View.VISIBLE);
            } else {
                sendOutTv.setVisibility(View.GONE);
            }
        }

        Date date = new Date();
        date.setTime(Long.parseLong(goodInfo.getTime()) * 1000);
        timeTv.setText(new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(date));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_help_me_back:
                finish();
                break;
            case R.id.tv_to_pay:
                Intent intent = new Intent(this, PaymentActivity.class);
                intent.putExtra("price", goodInfo.getMoney());
                intent.putExtra("tradeNo", goodInfo.getOrder_num());
                intent.putExtra("orderType", 2);//1--任务,2--商品
                startActivityForResult(intent, ORDER_DETAIL_REQUEST_CODE);
                break;
            case R.id.tv_cancel_payment:
                new AlertDialog.Builder(this).setTitle("取消订单").setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrde();
                    }
                }).setNegativeButton("取消", null).show();
                break;
            case R.id.tv_comment:
                Intent intent1 = new Intent(this, PostCommentActivity.class);
                intent1.putExtra("order_id", goodInfo.getId());
                intent1.putExtra("type", "2");//任务是1,商城是2
                startActivityForResult(intent1, ORDER_DETAIL_REQUEST_CODE);
                break;
            case R.id.tv_send_out:
                sendOutOrder();
                break;
        }
    }

    private void cancelOrde() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_num", goodInfo.getOrder_num());
        requestParams.put("state", OrderHelper.CANCELED);
        HelperAsyncHttpClient.get(NetConstant.UPDATE_SHOPPING_STATE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(MyOrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                            setResult(CANCEL_SUCCESS);
                            cancelPaymentTv.setVisibility(View.GONE);
                            toPaytv.setVisibility(View.GONE);
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(MyOrderDetailActivity.this, data, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void sendOutOrder() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("order_num", goodInfo.getOrder_num());
        requestParams.put("state", OrderHelper.UNCONFIRMED);
        HelperAsyncHttpClient.get(NetConstant.UPDATE_SHOPPING_STATE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(MyOrderDetailActivity.this, "提醒发货成功", Toast.LENGTH_SHORT).show();
                            sendOutTv.setVisibility(View.GONE);
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(MyOrderDetailActivity.this, data, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ORDER_DETAIL_REQUEST_CODE) {
            switch (resultCode) {
                case COMMENT_SUCCESS:
                    commentTv.setVisibility(View.GONE);
                    setResult(COMMENT_SUCCESS);
                    break;
                case PAY_SUCCESS:
                    toPaytv.setVisibility(View.GONE);
                    setResult(PAY_SUCCESS);
                    break;
            }


        }

    }
}
