package cn.xcom.helper.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.adapter.ViewPageAdapter;
import cn.xcom.helper.bean.AppInfo;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.model.CityBean;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.GetUniqueNumber;
import cn.xcom.helper.utils.SPUtils;
import cn.xcom.helper.utils.SingleVolleyRequest;
import cn.xcom.helper.utils.StringPostRequest;
import cn.xcom.helper.utils.ToastUtils;
import cz.msebera.android.httpclient.Header;

import static com.baidu.mapapi.BMapManager.getContext;

/**
 * Created by zhuchongkun on 16/6/17.
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private String TAG="SplashActivity";
    private Context mContext;
    private ViewPager viewPager;
    private AppInfo appInfo;
    private ViewPageAdapter viewPageAdapter;
    private List<ImageView> views;
    private static final int[] pics = new int[]{R.mipmap.ic_splash_welcome_first, R.mipmap.ic_splash_welcome_second, R.mipmap.ic_splash_welcome_third};
    private LinearLayout ll_point;
    private TextView tv;
    private ImageView[] points;
    private int currentIndex;
    private UserInfo userInfo;
    List<CityBean>list;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        View view = View.inflate(mContext, R.layout.activity_splash, null);
        setContentView(view);
        //判断是否是第一次进入此版本
        appInfo = new AppInfo();
        if (appInfo.isFristLogin(mContext)) {
            //是，则viewPager
            initViewPager();
        } else {
            //否，单页动画
            initLoding(view);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(mContext);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(mContext);
    }

    private void initViewPager() {
        views = new ArrayList<ImageView>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(pics[i]);
            views.add(i, iv);
        }
        viewPager = (ViewPager) findViewById(R.id.viewpager_splash);
        viewPager.setVisibility(View.VISIBLE);
        viewPageAdapter = new ViewPageAdapter(views,mContext);
        viewPager.setAdapter(viewPageAdapter);
        viewPager.setOnPageChangeListener(this);
        tv = (TextView)findViewById(R.id.tv_splash_into);
        tv.setVisibility(View.GONE);
        tv.setOnClickListener(this);
        ll_point= (LinearLayout) findViewById(R.id.ll_splash);
        ll_point.setVisibility(View.VISIBLE);
        points=new ImageView[pics.length];
        for (int i=0;i<pics.length;i++){
            points[i]= (ImageView) ll_point.getChildAt(i);
            points[i].setEnabled(true);
            points[i].setOnClickListener(this);
            points[i].setTag(i);
        }
        currentIndex=0;
        points[currentIndex].setEnabled(false);
    }

    private void initLoding(View view) {
        view.setBackgroundResource(R.mipmap.ic_splash_loading);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.3f, 1.0f);
        alphaAnimation.setDuration(1500);
        view.startAnimation(alphaAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                LoginIn();
            }
        }, 3000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_splash_into:
                try {
                    PackageManager pm = mContext.getPackageManager();
                    PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
                    String currentVersion = "" + pi.versionCode;
                    appInfo.setLastVersionCode(currentVersion);
                    appInfo.writeData(mContext);
                    userInfo = new UserInfo(mContext);
                  if (userInfo.isLogined()) {
                        LoginIn();
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        finish();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            default:
                int position = (Integer) v.getTag();
                if (position >= 0 && position < pics.length) {
                    viewPager.setCurrentItem(position);
                    setCurrentDat(position);
                }
                break;
        }

    }
    private void LoginIn(){
            final UserInfo userInfo=new UserInfo(mContext);
            userInfo.readData(mContext);
            if (!userInfo.getUserId().isEmpty()){
                RequestParams params = new RequestParams();
                params.put("userid", userInfo.getUserId());
                HelperAsyncHttpClient.get(NetConstant.CHECK_LOGIN, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        String id = GetUniqueNumber.getInstance().getNumber();
                        if (response.optString("status").equals("success")) {
                            if(response.optString("data").equals(id)){
                                getNameAuthentication(userInfo.getUserId());
                            }else{
                                startActivity(new Intent(mContext, LoginActivity.class));
                            }

                        } else {
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        popLogOutDialog("网络错误","网络连接超时,请检查您的网络");
                    }

                });
            }else{
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }

    }

    private void popLogOutDialog(String title, String message) {
        List<Activity> activities = HelperApplication.getInstance().getActivities();
        if (activities.size() == 0) {
            return;
        }
        final Activity activity = activities.get(activities.size() - 1);
        if (activity == null) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title).setMessage(message).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserInfo userInfo = new UserInfo();
                userInfo.clearDataExceptPhone(activity);
                SPUtils.clear(activity);
                JPushInterface.stopPush(activity);
                activity.startActivity(new Intent(activity, LoginActivity.class));
                HelperApplication.getInstance().onTerminate();
            }
        });
        builder.show();

    }

    /**
     * 获取实名认证
     */
    private void getNameAuthentication(final String userid) {
        RequestParams params=new RequestParams();
        params.put("userid",userid);
        HelperAsyncHttpClient.get(NetConstant.Check_Had_Authentication, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "1");
                } else {
                    SPUtils.put(mContext, HelperConstant.IS_HAD_AUTHENTICATION, "0");
                }
                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra("userid", userid);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }
    private void setCurrentDat(int i) {
        if (i<0||i>pics.length-1||currentIndex==i){
            return;
        }
        points[i].setEnabled(false);
        points[currentIndex].setEnabled(true);
        currentIndex = i;
        if (i == pics.length - 1) {
            ll_point.setVisibility(View.GONE);
            tv.setVisibility(View.VISIBLE);
        } else {
            ll_point.setVisibility(View.VISIBLE);
            tv.setVisibility(View.GONE);
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setCurrentDat(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
