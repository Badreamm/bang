package cn.xcom.helper.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/10/11 0011.
 * 绑定账户页面
 */
public class BindAccountActivity extends BaseActivity implements View.OnClickListener {
    private TextView zfbAccountTv, bankAccountTv;
    private CheckBox zfbCheckBox, bankCheckBox;
    private UserInfo userInfo;
    private Button confirmButton;
    private boolean zfbRegistered = false;
    private boolean bankRegistered = false;
    private RelativeLayout zfbRelativeLayout, bankRelativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);
        userInfo = new UserInfo(this);
        initView();
        getBankInfo();
    }

    private void initView() {
        zfbAccountTv = (TextView) findViewById(R.id.tv_zhifubao_account);
        bankAccountTv = (TextView) findViewById(R.id.tv_bank_account);
        zfbCheckBox = (CheckBox) findViewById(R.id.cb_zhifubao);
        bankCheckBox = (CheckBox) findViewById(R.id.cb_bank);
        confirmButton = (Button) findViewById(R.id.bt_confirm);
        confirmButton.setOnClickListener(this);
        zfbRelativeLayout = (RelativeLayout) findViewById(R.id.rl_zfb);
        zfbRelativeLayout.setOnClickListener(this);
        bankRelativeLayout = (RelativeLayout) findViewById(R.id.rl_bank);
        bankRelativeLayout.setOnClickListener(this);
    }


    private void getBankInfo() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        HelperAsyncHttpClient.get(NetConstant.GET_USER_BANK_INFO, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String zfbAccount = jsonObject.getString("alipay");
                            if (!"".equals(zfbAccount)) {
                                zfbAccountTv.setText(zfbAccount);
                                zfbRegistered = true;
                            }
                            String bankAccount = jsonObject.getString("bank");
                            if (!"".equals(bankAccount)) {
                                bankAccountTv.setText(bankAccount);
                                bankRegistered = true;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_confirm:
                if (zfbCheckBox.isChecked() && !bankCheckBox.isChecked()) {
                    if (zfbRegistered) {
                        Toast.makeText(this, "已绑定", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(this,BindAlipayAccountActivity.class));
                    }
                } else if (!zfbCheckBox.isChecked() && bankCheckBox.isChecked()) {
                    if (bankRegistered) {
                        Toast.makeText(this, "已绑定", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(this, BindBankAccountActivity.class));
                    }
                }
                break;
            case R.id.rl_zfb:
                zfbCheckBox.setChecked(true);
                bankCheckBox.setChecked(false);
                break;
            case R.id.rl_bank:
                zfbCheckBox.setChecked(false);
                bankCheckBox.setChecked(true);
                break;
        }
    }

    public void onBack(View v) {
        finish();
    }
}
