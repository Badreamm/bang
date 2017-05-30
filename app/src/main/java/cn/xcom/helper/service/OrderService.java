package cn.xcom.helper.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.xcom.helper.activity.OrderTakingActivity;
import cn.xcom.helper.bean.TaskItemInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.ToastUtil;
import cz.msebera.android.httpclient.Header;


public class OrderService extends Service {

    public OrderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        timer.schedule(new Work(),0, 1000);
    }



    class Work extends TimerTask {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            Message message = new Message();
            message.what=1;
            handler.sendMessage(message);
        }
    }

    Handler handler = new Handler()
    {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if(msg.what==1)
            {
//                final UserInfo userInfo;
//                userInfo = new UserInfo();
//                userInfo.readData(getApplicationContext());
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("userid", userInfo.getUserId());
//
//                HelperAsyncHttpClient.get(NetConstant.GET_MY_NOAPPLY_HIRE_TAST_COUNT, requestParams,
//                        new JsonHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                                super.onSuccess(statusCode, headers, response);
//                                if (response != null) {
//                                    Log.d("===response", response.toString() + userInfo.getUserId());
//                                    try {
//                                        String state = response.getString("status");
//                                        String data = response.getString("data");
//                                        if (state.equals("success") && (!data.equals("0"))) {
//                                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                                    builder.setMessage("您有雇佣订单未处理，请前往处理");
//                                    builder.setTitle("提示");
//                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            getApplicationContext().startActivity(new Intent(getApplicationContext(), OrderTakingActivity.class));
//                                            Timer timer = new Timer();
//                                            timer.schedule(new Work(),0, 1000*60*5);
//                                            dialog.dismiss();
//
//                                        }
//                                    });
////
//                                    builder.show();
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//
//
//                            }
//                        });
                Log.d("===response", "eee");
            }
        }

    };
}
