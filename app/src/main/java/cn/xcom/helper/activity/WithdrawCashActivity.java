package cn.xcom.helper.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.HelperApplication;
import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;

/**
 * Created by zhuchongkun on 16/6/12.
 * 提现页
 */
public class WithdrawCashActivity extends BaseActivity implements View.OnClickListener{
    private String TAG="WithdrawCashActivity";
    private Context mContext;
    private RelativeLayout rl_back;
    private EditText et_money,et_account;
    private Button bt_present;
    private String account;
    private int bankType = 0;
    private UserInfo userInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_withdraw_cash);
        mContext=this;
        account = getIntent().getStringExtra("account");
        bankType = getIntent().getIntExtra("bankType", 0);
        userInfo = new UserInfo(this);
        initView();
    }

    private void initView(){
        rl_back= (RelativeLayout) findViewById(R.id.rl_withdraw_cash_back);
        rl_back.setOnClickListener(this);
        et_money= (EditText) findViewById(R.id.et_withdraw_cash_money);
        et_account= (EditText) findViewById(R.id.et_withdraw_cash_account);
        bt_present= (Button) findViewById(R.id.bt_withdraw_cash_present);
        bt_present.setOnClickListener(this);
        et_account.setText(account);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_withdraw_cash_back:
                finish();
                break;
            case R.id.bt_withdraw_cash_present:
                applyWithdraw();
                break;
        }

    }

    private void applyWithdraw(){
        double money;
        String moneyStr = et_money.getText().toString();
        if("".equals(moneyStr)){
            money = 0;
        }else{
            money =Double.parseDouble(moneyStr);
        }
        if(money < 100){
            Toast.makeText(WithdrawCashActivity.this,"提现金额最少100元",Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid",userInfo.getUserId());
        requestParams.put("money",money);
        requestParams.put("banktype", bankType);
        HelperAsyncHttpClient.get(NetConstant.APPLY_WITHDRAW,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(WithdrawCashActivity.this, "提现申请成功，请耐心等待", Toast.LENGTH_SHORT).show();
                            finish();
                            HelperApplication.getInstance().isBack = true;
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(WithdrawCashActivity.this, data, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

}
