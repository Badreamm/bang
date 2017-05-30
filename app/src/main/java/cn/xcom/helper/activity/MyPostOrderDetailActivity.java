package cn.xcom.helper.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.xcom.helper.R;
import cn.xcom.helper.adapter.CommentsListAdapter;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.order.MyPostOrderFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.MyImageLoader;
import cz.msebera.android.httpclient.Header;

/**
 * 我的发单详情页面
 */

public class MyPostOrderDetailActivity extends BaseActivity implements View.OnClickListener {
    private int titleType;
    private TextView fistTitle, secondTitle, thirdTitle, fourthTitle, orderNumTv, descriptionTv,
            priceTv, saddressTv, addressTv, expiryTv, applyPhoneTv, timeTv, serverNameTv, serveCountTv;
    private ImageView avatarIv, toPhoneIv, toChatIv;
    private String orderId;
    private Button cancelBtn;
    private UserInfo userInfo;
    private Context mContext;
    private ListView commentsLv;
    private TaskItemInfo taskItemInfo;
    private RelativeLayout applyRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post_order_detail);
        mContext = this;
        userInfo = new UserInfo(this);
//        titleType = getIntent().getIntExtra("type", 0);
//        orderId = getIntent().getStringExtra("taskid");
        Bundle bundle = getIntent().getBundleExtra("bundle");
        titleType = bundle.getInt("type");
        taskItemInfo = (TaskItemInfo) bundle.getSerializable("task_info");
        initView();
        setView(taskItemInfo);
//        getData();
    }

    private void initView() {
        fistTitle = (TextView) findViewById(R.id.tv_title_first);
        secondTitle = (TextView) findViewById(R.id.tv_title_second);
        thirdTitle = (TextView) findViewById(R.id.tv_title_third);
        fourthTitle = (TextView) findViewById(R.id.tv_title_fourth);
        orderNumTv = (TextView) findViewById(R.id.tv_order_num);
        descriptionTv = (TextView) findViewById(R.id.tv_description);
        priceTv = (TextView) findViewById(R.id.tv_price);
        saddressTv = (TextView) findViewById(R.id.tv_saddress);
        addressTv = (TextView) findViewById(R.id.tv_address);
        expiryTv = (TextView) findViewById(R.id.tv_expiry_time);
        applyPhoneTv = (TextView) findViewById(R.id.tv_apply_phone);
        cancelBtn = (Button) findViewById(R.id.btn_cancel);
        if (taskItemInfo.getState().equals("-1")) {
            cancelBtn.setBackgroundResource(R.color.alpha_gray);
            cancelBtn.setClickable(false);
        } else {
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext).setTitle("取消订单").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPayment();
                        }
                    }).setNegativeButton("取消", null).show();
                }
            });
        }
        commentsLv = (ListView) findViewById(R.id.lv_comments);
        timeTv = (TextView) findViewById(R.id.tv_time);
        avatarIv = (ImageView) findViewById(R.id.iv_avatar);
        serverNameTv = (TextView) findViewById(R.id.tv_server_name);
        serveCountTv = (TextView) findViewById(R.id.tv_serve_count);
        toPhoneIv = (ImageView) findViewById(R.id.iv_to_phone);
        toPhoneIv.setOnClickListener(this);
        toChatIv = (ImageView) findViewById(R.id.iv_to_chat);
        toChatIv.setOnClickListener(this);
        applyRl = (RelativeLayout) findViewById(R.id.rl_apply);
    }

    private void setView(TaskItemInfo taskItemInfo) {
        switch (titleType) {
            case 1:
                fistTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                cancelBtn.setVisibility(View.VISIBLE);
                break;
            case 2:
                secondTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                cancelBtn.setVisibility(View.VISIBLE);
                break;
            case 3:
                thirdTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
                break;
            case 4:
                fourthTitle.setBackgroundColor(getResources().getColor(R.color.colorTheme));
        }
        orderNumTv.setText(taskItemInfo.getOrder_num());
        descriptionTv.setText(taskItemInfo.getDescription());
        priceTv.setText(taskItemInfo.getPrice());
        saddressTv.setText(taskItemInfo.getSaddress());
        addressTv.setText(taskItemInfo.getAddress());
        Date date = new Date();
        date.setTime(Long.parseLong(taskItemInfo.getExpirydate()) * 1000);
        expiryTv.setText(new SimpleDateFormat("MM-dd HH:mm:ss").format(date));
        if (taskItemInfo.getApply() == null) {
            applyPhoneTv.setText("无人接单");
            applyRl.setVisibility(View.GONE);
        } else {
            applyPhoneTv.setText(taskItemInfo.getApply().getPhone());
            serverNameTv.setText(taskItemInfo.getApply().getName());
//            serveCountTv.setText(taskItemInfo.getApply());
            if ("".equals(taskItemInfo.getApply().getPhoto())) {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG, avatarIv);
            } else {
                MyImageLoader.display(NetConstant.NET_DISPLAY_IMG +
                        taskItemInfo.getApply().getPhoto(), avatarIv);
            }
        }
        CommentsListAdapter adapter = new CommentsListAdapter(mContext, taskItemInfo.getEvaluate());
        commentsLv.setAdapter(adapter);

        date.setTime(Long.parseLong(taskItemInfo.getTime()) * 1000);
        timeTv.setText(new SimpleDateFormat("MM-dd HH:mm:ss").format(date));

    }

    private void getData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("taskid", orderId);
        HelperAsyncHttpClient.get(NetConstant.GET_TASK_INFO_BY_TASK_ID, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    String data = response.getString("data");
                                    TaskItemInfo taskItemInfo = new Gson().fromJson(data,
                                            new TypeToken<TaskItemInfo>() {
                                            }.getType());
                                    setView(taskItemInfo);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void cancelPayment() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("ownerid", userInfo.getUserId());
        requestParams.put("taskid", taskItemInfo.getId());
        HelperAsyncHttpClient.get(NetConstant.OWNER_CANCEL_TASK, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(MyPostOrderDetailActivity.this, "取消订单成功", Toast.LENGTH_SHORT).show();
                                    cancelBtn.setVisibility(View.GONE);
                                    setResult(MyPostOrderFragment.COMMENT_RESULT_CODE);
                                } else {
                                    Toast.makeText(MyPostOrderDetailActivity.this, "取消订单失败", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }

    public void onBack(View v) {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_to_phone:
                requestPermission();
                break;
            case R.id.iv_to_chat:
                Intent chatIntent = new Intent(this, ChatActivity.class);
                chatIntent.putExtra("id", taskItemInfo.getApply().getUserId());
                chatIntent.putExtra("name", taskItemInfo.getApply().getName());
                startActivity(chatIntent);
                break;
        }
    }

    private void callPhone() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + taskItemInfo.getApply().getPhone()));
        //开启系统拨号器
        startActivity(intent);
    }

    int PHONE_REQUEST_CODE = 1;

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            // 第一次请求权限时，用户如果拒绝，下一次请求shouldShowRequestPermissionRationale()返回true
            // 向用户解释为什么需要这个权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                new AlertDialog.Builder(this)
                        .setMessage("申请电话权限")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //申请电话权限
                                ActivityCompat.requestPermissions(MyPostOrderDetailActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE}, PHONE_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //申请电话权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, PHONE_REQUEST_CODE);
            }
        } else {
            callPhone();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PHONE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                callPhone();
            } else {
                //用户勾选了不再询问
                //提示用户手动打开权限
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    Toast.makeText(this, "拨打电话权限已被禁止", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }


}
