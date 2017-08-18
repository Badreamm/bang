package cn.xcom.helper.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.HelperConstant;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.fragment.BuyFragment;
import cn.xcom.helper.fragment.EmptyFragment;
import cn.xcom.helper.fragment.MapFragment;
import cn.xcom.helper.fragment.MeFragment;
import cn.xcom.helper.fragment.OneYuanBuyFragment;
import cn.xcom.helper.fragment.SaleFragment;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.SPUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/5/27.
 * 主页面
 */
public class HomeActivity extends AppCompatActivity {
    private Context mContext;
    private Button[] mTabs;
    private TextView unReadMap, unReadBuy, unReadSale, unReadMe;
    private MapFragment mapFragment;
    private SaleFragment saleFragment;
    private MeFragment meFragment;
//    private OneYuanBuyFragment oneYuanBuyFragment;
    private EmptyFragment emptyFragment;
    private Fragment[] fragments;
    private UserInfo userInfo;
    private int a = 0;
    private int index;
    private int currentTanIndex;
    private String from = "";
    private String state;
    private int flag=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        mContext = this;
        userInfo = new UserInfo(mContext);
        userInfo.readData(mContext);
        initView();
        initFragment();
        state = SPUtils.get(mContext,HelperConstant.IS_HAD_AUTHENTICATION,"").toString();
        if(state.equals("0")){
            mTabs[1].setText("分享礼");
        }else if(state.equals("1")){
            mTabs[1].setText("分享礼");
        }
        getOrder(userInfo.getUserId());
        HelperApplication.getInstance().addActivity(this);
        getBreak();
}

    private void initView() {
        unReadMap = (TextView) findViewById(R.id.tv_home_map_red);
        unReadBuy = (TextView) findViewById(R.id.tv_home_buy_red);
        unReadSale = (TextView) findViewById(R.id.tv_home_sale_red);
        unReadMe = (TextView) findViewById(R.id.tv_home_me_red);
        mTabs = new Button[4];
        mTabs[0] = (Button) findViewById(R.id.bt_home_map);
        mTabs[1] = (Button) findViewById(R.id.bt_home_buy);
        mTabs[2] = (Button) findViewById(R.id.bt_home_sale);
        mTabs[3] = (Button) findViewById(R.id.bt_home_me);
        mTabs[a].setSelected(true);
    }

    private void initFragment() {
        mapFragment = new MapFragment();
//        buyFragment = new BuyFragment();
        saleFragment = new SaleFragment();
        meFragment = new MeFragment();
//        oneYuanBuyFragment = new OneYuanBuyFragment();
        emptyFragment = new EmptyFragment();
        fragments = new Fragment[]{mapFragment, emptyFragment, saleFragment, meFragment};
        switch (a) {
            case 0:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, mapFragment)
                        .add(R.id.rl_home_fragment_container, emptyFragment)
                        .hide(emptyFragment)
                        .show(mapFragment).commit();
                break;
            case 1:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, mapFragment)
                        .add(R.id.rl_home_fragment_container, emptyFragment)
                        .add(R.id.rl_home_fragment_container, saleFragment)
                        .hide(mapFragment)
                        .show(emptyFragment)
                        .hide(saleFragment).commit();
                break;
            case 2:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, emptyFragment)
                        .add(R.id.rl_home_fragment_container, saleFragment)
                        .add(R.id.rl_home_fragment_container, meFragment)
                        .hide(emptyFragment)
                        .show(saleFragment)
                        .hide(meFragment).commit();
                break;
            case 3:
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.rl_home_fragment_container, saleFragment)
                        .add(R.id.rl_home_fragment_container, meFragment)
                        .hide(saleFragment)
                        .show(meFragment).commit();
                break;

        }

    }

    public void onTabClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_home_map:
                index = 0;
                break;
            case R.id.bt_home_buy:
                index = 1;
                break;
            case R.id.bt_home_sale:
                index = 2;
                break;
            case R.id.bt_home_me:
                index = 3;
                break;
        }
        if (currentTanIndex != index) {
            if(index == 1 && state != SPUtils.get(mContext,HelperConstant.IS_HAD_AUTHENTICATION,"").toString()&&flag==0){
                flag = 1;
                fragments[1] = new BuyFragment();
                mTabs[1].setText("分享礼");
            }
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTanIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.rl_home_fragment_container, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        mTabs[currentTanIndex].setSelected(false);
        mTabs[index].setSelected(true);
        currentTanIndex = index;
    }

    public void checkToSecond() {
        FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
        trx.hide(fragments[currentTanIndex]);
        if (!fragments[1].isAdded()) {
            trx.add(R.id.rl_home_fragment_container, fragments[1]);
        }
        trx.show(fragments[1]).commit();
        mTabs[currentTanIndex].setSelected(false);
        mTabs[1].setSelected(true);
        currentTanIndex = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        getInsurance();

    }



    /**
     * 获取保险认证
     */
    private void getInsurance() {
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.Check_Insurance, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.e("认证", response.toString());
                if (response.optString("status").equals("success")) {
                    SPUtils.put(mContext, HelperConstant.IS_INSURANCE, response.optString("data"));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Log.e("认证", responseString);
            }
        });
    }

    /**
     * 获取保险认证
     */
    private void getBreak() {
        RequestParams params = new RequestParams();
        HelperAsyncHttpClient.get("http://wxxcs.ngrok.cc/test/version", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if(response.optInt("status")==200 && response.optString("message").equals("break")){
                    finish();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    @Override
    protected void onResumeFragments() {//在onResumeFragment里做fragment transaction操作
        super.onResumeFragments();
        from = getIntent().getStringExtra("from");
        Log.d("HomeActivity","out");
        if("push".equals(from)){
            Log.d("HomeActivity","in");
            onTabClicked(findViewById(R.id.bt_home_buy));
        }

    }

    @Override
    protected void onDestroy() {
//        Intent stopintent = new Intent(this, OrderService.class);
//        stopService(stopintent);
        super.onDestroy();
    }
    private void getOrder(final String userId) {
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

                                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                    builder.setMessage("您有雇佣订单未处理，请前往处理");
                                    builder.setTitle("提示");
                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(mContext, OrderTakingActivity.class));
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
    }
}
