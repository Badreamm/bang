package cn.xcom.helper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.LogUtils;
import cn.xcom.helper.utils.RegexUtil;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/12 0012.
 * 绑定银行卡界面
 */
public class BindBankAccountActivity extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "BindBankAccountActivity";
    private EditText nameEditText, idEditText, accountEditText, codeEditText, bankNameEditText;
    private TextView getCodeTv, phoneTv;
    private Button bindButton;
    private UserInfo userInfo;
    private String userPhone,binkAccount;
    private int time;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_bank_account);
        initView();
        userInfo = new UserInfo(this);
        userPhone = userInfo.getUserPhone();
        timer = new Timer();
        String decodePhone = userPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        String phoneTxt = "您当前绑定的手机号码:" + decodePhone;
        phoneTv.setText(phoneTxt);
    }

    private void initView() {
        nameEditText = (EditText) findViewById(R.id.et_bind_account_name);
        idEditText = (EditText) findViewById(R.id.et_id_num);
        accountEditText = (EditText) findViewById(R.id.et_bind_account);
        codeEditText = (EditText) findViewById(R.id.et_bind_code);
        bindButton = (Button) findViewById(R.id.bt_bind_account_bind);
        getCodeTv = (TextView) findViewById(R.id.get_code);
        phoneTv = (TextView) findViewById(R.id.bind_phone);
        bankNameEditText = (EditText) findViewById(R.id.et_bank_name);
        getCodeTv.setOnClickListener(this);
        bindButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_code:
                getTextCode();
                break;
            case R.id.bt_bind_account_bind:
                bindCount();
                break;
        }
    }

    //获取短信验证码
    private void getTextCode() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", userPhone);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_CODE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            JSONObject jsonObject = response.getJSONObject("data");
                            String code = jsonObject.getString("code");
                            LogUtils.e(TAG, "--code->" + code);
                            Toast.makeText(BindBankAccountActivity.this, "发送成功，请注意接受！", Toast.LENGTH_LONG).show();
                            counter();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + responseString);
            }
        });
    }

    private void counter(){
        getCodeTv.setClickable(false);
        time = 120;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(time <=0){
                            getCodeTv.setClickable(true);
                            getCodeTv.setText("获取验证码");
                        }else{
                            getCodeTv.setClickable(false);
                            getCodeTv.setText("获取验证码(" + time + ")");
                        }
                        time --;
                    }
                });
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    private void bindCount() {
        String name = nameEditText.getText().toString();
        if (!RegexUtil.IsChineseOrEnglish(name)) {
            Toast.makeText(BindBankAccountActivity.this, "输入的姓名不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String idNum = idEditText.getText().toString();
        if (!RegexUtil.checkIdCard(idNum)) {
            Toast.makeText(BindBankAccountActivity.this, "输入的身份证号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String binkName = bankNameEditText.getText().toString();
        if (bankNameEditText.length() == 0) {
            Toast.makeText(BindBankAccountActivity.this, "输入的开户银行不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        binkAccount = accountEditText.getText().toString();
        if (!(binkAccount.length() == 16 || binkAccount.length() == 19|| binkAccount.length() == 18)) {
            Toast.makeText(BindBankAccountActivity.this, "输入的银行号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        String securityCode = codeEditText.getText().toString();
        if (securityCode.length() == 0) {
            Toast.makeText(BindBankAccountActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", userInfo.getUserId());
        requestParams.put("realname", name);
        requestParams.put("idcard", idNum);
        requestParams.put("bank", binkName);
        requestParams.put("bankno", binkAccount);
        requestParams.put("phone", userPhone);
        requestParams.put("code", securityCode);

        HelperAsyncHttpClient.get(NetConstant.BIND_BINK_ACCOUNT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                LogUtils.e(TAG, "--statusCode->" + statusCode + "==>" + response.toString());
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(BindBankAccountActivity.this, "绑定成功！", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(BindBankAccountActivity.this, WithdrawCashActivity.class);
                            intent.putExtra("account",binkAccount);
                            intent.putExtra("bankType", 2);
                            startActivity(intent);
                        }else{
                            String data = response.getString("data");
                            Toast.makeText(BindBankAccountActivity.this,data, Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public void back(View v) {
        finish();
    }

}
