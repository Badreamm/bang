package cn.xcom.helper.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.activity.OrderTakingActivity;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.ToastUtil;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
//        Intent i = new Intent(context, OrderService.class);
//        context.startService(i);
        ToastUtil.showShort(context,"111");
        final UserInfo userInfo;
        userInfo = new UserInfo();
        userInfo.readData(context);
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());

        HelperAsyncHttpClient.get(NetConstant.GET_MY_NOAPPLY_HIRE_TAST_COUNT, requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            Log.d("===response", response.toString() + userInfo.getUserId());
                            try {
                                String state = response.getString("status");
                                String data = response.getString("data");
                                if (state.equals("success") && (!data.equals("0"))) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage("您有雇佣订单未处理，请前往处理");
                                    builder.setTitle("提示");
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            context.startActivity(new Intent(context, OrderTakingActivity.class));
                                            dialog.dismiss();

                                        }
                                    });

                                    builder.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                });
//        String msg = intent.getStringExtra("msg");
//        Toast.makeText(context,msg, Toast.LENGTH_SHORT).show();
    }
}