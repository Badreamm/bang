package cn.xcom.helper.activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.service.OrderService;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.ToastUtil;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 基础页
 */
public class BaseActivity extends FragmentActivity {
//
//    private IntentFilter intentFilter;
//    private LocalReceiver localReceiver;
//    private LocalBroadcastManager lbm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        IntentFilter filter = new IntentFilter("com.USER_ACTION");
//        this.registerReceiver(new Receiver(), filter);
        //添加ctivity集合
        HelperApplication.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
//        lbm = LocalBroadcastManager.getInstance(this);
//
//        intentFilter = new IntentFilter();
//        intentFilter.addAction("com.example.broadcasttest.MY_BROADCAST");
//        localReceiver = new LocalReceiver();
//        lbm.registerReceiver(localReceiver, intentFilter);
    }

    /**
     * 接受推送通知并通知页面添加小红点
     */
//    public class Receiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.i("Recevier1", "接收到:");
//            String key = SPUtils.get(context, "push", "").toString();
//            String state = SPUtils.get(context,"pushstate","").toString();
//            String title = "";
//            String message = "";
//            switch (key){
//                case "newTask":
//                    title = "所在城市有新的任务";
//                    if(state.equals("1")){
//                        message = "是否去抢单？";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "newMessage":
//                    title = "您有新的留言消息";
//                    message ="是否立即查看？";
//                    popDialog(context,title,message);
//                    break;
//                case "sendTaskType":
//                    title = "您的任务状态改变";
//                    if(state.equals("1")){
//                        message = "已被抢";
//                    }
//                    if(state.equals("2")){
//                        message = "对方已上门";
//                    }
//                    if(state.equals("3")){
//                        message = "对方已申请付款";
//                    }
//                    if(state.equals("4")){
//                        message = "对方已上门";
//                    }
//                    if(state.equals("5")){
//                        message = "对方已评价";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "acceptTaskType":
//                    title = "您接的任务状态改变";
//                    if(state.equals("1")){
//                        message = "对方已同意您接单";
//                    }
//                    if(state.equals("2")){
//                        message = "对方已付款";
//                    }
//                    if(state.equals("3")){
//                        message = "对方已评价";
//                    }
//                    if(state.equals("-1")){
//                        message = "对方已取消";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "myWallet":
//                    title = "钱包数据已更新";
//                    message = "是否立即查看？";
//                    popDialog(context,title,message);
//                    break;
//                case "buyOrderType":
//                    title = "您购买的商品订单状态已更新";
//                    if(state.equals("1")){
//                        message = "商家已接单";
//                    }
//                    if(state.equals("2")){
//                        message = "商家已发货";
//                    }
//                    if(state.equals("3")){
//                        message = "商品已送达/订单已消费";
//                    }
//                    if(state.equals("4")){
//                        message = "商家回复评论";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "businessOrderType":
//                    title = "您发布的商品订单状态已更新";
//                    if(state.equals("1")){
//                        message = "有人购买您的商品";
//                    }
//                    if(state.equals("2")){
//                        message = "对方已取消订单";
//                    }
//                    if(state.equals("3")){
//                        message = "对方已付款";
//                    }
//                    if(state.equals("4")){
//                        message = "订单已消费";
//                    }
//                    if(state.equals("5")){
//                        message = "对方已评论";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "loginFromOther":
//                    title = "有人登陆您的账号";
//                    message ="您需要重新登陆";
//                    Intent loginIntent = new Intent(context,HomeActivity.class);
////                    HelperApplication.getInstance().flag = "true";
//                    startActivity(loginIntent);
//                    break;
//                case "certificationType":
//                    title = "认证状态更新";
//                    if(state.equals("1")){
//                        message = "您已通过身份认证";
//                    }
//                    if(state.equals("2")){
//                        message = "您未通过身份认证";
//                    }
//                    if(state.equals("3")){
//                        message = "您已通过投保认证";
//                    }
//                    if(state.equals("4")){
//                        message = "您未通过投保认证";
//                    }
//                    popDialog(context,title,message);
//                    break;
//                case "prohibitVisit":
//                    title = "封号";
//                    message = "封号";
//                    popDialog(context,title,message);
//                    break;
//            }
//        }

//    }

    /**
     * 接受推送弹出提示框
     *
     * @param context
     * @param title
     * @param message
     */
    private void popDialog(final Context context, String title, String message) {
        AlertView mAlertView = new AlertView(title, message, "取消", new String[]{"确定"}, null, context, AlertView.Style.Alert, new OnItemClickListener() {
            @Override
            public void onItemClick(Object o, int position) {
                if (position == 0) {
                    ToastUtil.showShort(context, "确定");
                }
            }
        }).setCancelable(true).setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {

            }
        });
        mAlertView.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isBackground(getApplicationContext());
        //注：回调 1
//        Bugtags.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //注：回调 2
//       Bugtags.onPause(this);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        HelperApplication.getInstance().finishActivity(this);
        super.onDestroy();
//        lbm.unregisterReceiver(localReceiver);
    }
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        //注：回调 3
//        Bugtags.onDispatchTouchEvent(this, event);
//        return super.dispatchTouchEvent(event);
//    }

    /**
     * 判断当前应用程序处于前台还是后台
     */
    private boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
                    Log.i("前台", appProcess.processName);

//                    Intent intent = new Intent("com.example.broadcasttest.MY_BROADCAST");
//                    lbm.sendBroadcast(intent);
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
//
//    class LocalReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(context, "recaived local broadcast", Toast.LENGTH_SHORT).show();
//            UserInfo userInfo;
//            userInfo = new UserInfo();
//            userInfo.readData(context);
//            getOrder(userInfo.getUserId(), context);
//        }

        private void getOrder(final String userId, final Context context) {
            RequestParams requestParams = new RequestParams();
            requestParams.put("userid", userId);

            HelperAsyncHttpClient.get(NetConstant.GET_MY_NOAPPLY_HIRE_TAST_COUNT, requestParams,
                    new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            if (response != null) {
                                Log.d("===response", response.toString() + userId);
                                try {
                                    String state = response.getString("status");
                                    String data = response.getString("data");
                                    if (state.equals("success") && (!data.equals("0"))) {
//
//                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                        builder.setMessage("您有雇佣订单未处理，请前往处理");
//                                        builder.setTitle("提示");
//                                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                startActivity(new Intent(context, OrderTakingActivity.class));
//                                                dialog.dismiss();
//
//                                            }
//                                        });
//
//                                        builder.show();

                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("标题");
                                        builder.setMessage("提示文字");
                                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //增加按钮,回调事件
                                                    }
                                                }
                                        );
                                        builder.setCancelable(false);//弹出框不可以换返回键取消
                                        AlertDialog dialog = builder.create();
                                        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//将弹出框设置为全局
                                        dialog.setCanceledOnTouchOutside(false);//失去焦点不会消失
                                        dialog.show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    });
        }

}
