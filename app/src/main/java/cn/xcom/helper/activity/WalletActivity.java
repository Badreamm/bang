package cn.xcom.helper.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.bean.WalletInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/12.
 * 钱包页
 */
public class WalletActivity extends BaseActivity implements View.OnClickListener {
    private String TAG = "WalletActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private TextView tv_momeny, tv_month_singular, tv_month_income, tv_all_singular, tv_all_income, tv_know_more;
    private Button bt_present;
    private LinearLayout ll_present, ll_income, ll_my_work, ll_bind;
    private UserInfo userInfo;
    private WalletInfo walletInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_wallet);
        mContext = this;
        initView();
    }

    private void initView() {
        rl_back = (RelativeLayout) findViewById(R.id.rl_wallet_back);
        rl_back.setOnClickListener(this);
        ll_my_work = (LinearLayout) findViewById(R.id.ll_wallet_my_work);
        ll_my_work.setOnClickListener(this);
        tv_momeny = (TextView) findViewById(R.id.tv_wallet_money);
        tv_month_singular = (TextView) findViewById(R.id.tv_wallet_month_singular_number);
        tv_month_income = (TextView) findViewById(R.id.tv_wallet_month_income);
        tv_all_singular = (TextView) findViewById(R.id.tv_wallet_all_singular_number);
        tv_all_income = (TextView) findViewById(R.id.tv_wallet_all_income);
        ll_present = (LinearLayout) findViewById(R.id.ll_wallet_present_records);
        ll_present.setOnClickListener(this);
        ll_income = (LinearLayout) findViewById(R.id.ll_wallet_income_records);
        ll_income.setOnClickListener(this);
        bt_present = (Button) findViewById(R.id.bt_wallet_present);
        bt_present.setOnClickListener(this);
        userInfo = new UserInfo(mContext);
        walletInfo = new WalletInfo();
        tv_know_more = (TextView) findViewById(R.id.tv_know_more);
        tv_know_more.setOnClickListener(this);
        ll_bind = (LinearLayout) findViewById(R.id.ll_band_account);
        ll_bind.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWallet();
        if (HelperApplication.getInstance().isBack) {
            showDialog();
        }
    }

    /**
     * 弹出提现提示框
     */
    private void showDialog() {
        HelperApplication.getInstance().isBack = false;
        final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("提示").setMessage("提现已请求，最迟次日打账").setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wallet_back:
                finish();
                break;
            case R.id.ll_wallet_present_records:
                startActivity(new Intent(mContext, PresentRecordActivity.class));
                break;
            case R.id.ll_wallet_income_records:
                startActivity(new Intent(mContext, IncomeRecordsActivity.class));
                break;
            case R.id.bt_wallet_present:
//                startActivity(new Intent(mContext,BindAccountActivity.class));
                startActivity(new Intent(mContext, ChooseAccountActivity.class));

                break;
            case R.id.tv_know_more:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("title", "用户提现");
                intent.putExtra("url", "http://www.my51bang.com/index.php?g=portal&m=article&a=index&id=3");
                startActivity(intent);
                break;

            case R.id.ll_wallet_my_work:
                startActivity(new Intent(mContext, MyWorkActivity.class));
                break;
            case R.id.ll_band_account:
                startActivity(new Intent(mContext, BindAccountActivity.class));
                break;
        }
    }

    private void getWallet() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.NET_GET_WALLET, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            walletInfo.setMomney(jsonObject.getString("money"));
                            walletInfo.setAvailableMomney(jsonObject.getString("availablemoney"));
                            walletInfo.setAllTasks(jsonObject.getString("alltasks"));
                            walletInfo.setMonthTasks(jsonObject.getString("monthtasks"));
                            walletInfo.setAllIncome(jsonObject.getString("allincome"));
                            walletInfo.setMonthIncome(jsonObject.getString("monthincome"));
                            displayWallet();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    private void displayWallet() {
//        tv_momeny,tv_month_singular,tv_month_income,tv_all_singular,tv_all_income;
        tv_momeny.setText(walletInfo.getAvailableMomney());
        tv_all_singular.setText(walletInfo.getAllTasks());
        tv_month_singular.setText(walletInfo.getMonthTasks());
        tv_all_income.setText(walletInfo.getAllIncome());
        tv_month_income.setText(walletInfo.getMonthIncome());
    }
}
