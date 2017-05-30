package cn.xcom.helper.fragment.Authorized;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

import cn.xcom.helper.R;
import cn.xcom.helper.bean.UserInfo;
import cn.xcom.helper.constant.NetConstant;
import cn.xcom.helper.net.HelperAsyncHttpClient;
import cn.xcom.helper.utils.RegexUtil;
import cz.msebera.android.httpclient.Header;

/**
 * 绑定账号认证
 */
public class BindAccountAuthorizedFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private EditText relaNameEt, idNumEt, alipayAccountEt, bankNameEt, bankAccountEt, bindCodeEt;
    private TextView getCodeTv, bindPhoneTv;
    private Button submitBt;
    private CheckBox alipayCb, bankCb;
    private LinearLayout alipayLl, bankLl;
    private UserInfo userInfo;
    private String userPhone, decodePhone;
    private Timer timer;
    private int time;
    private Context mContext;
    private WeakReference<FragmentActivity> weakReference;
    private FragmentActivity fragmentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bind_account_authorized, container, false);
        initData();
        initView(view);
        return view;
    }

    private void initData() {
        mContext = getContext();
        userInfo = new UserInfo(getContext());
        userPhone = userInfo.getUserPhone();
        decodePhone = userPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        timer = new Timer();
    }

    private void initView(View v) {
        relaNameEt = (EditText) v.findViewById(R.id.et_real_name);
        idNumEt = (EditText) v.findViewById(R.id.et_id_num);
        alipayAccountEt = (EditText) v.findViewById(R.id.et_alipay_account);
        bankNameEt = (EditText) v.findViewById(R.id.et_bank_name);
        bankAccountEt = (EditText) v.findViewById(R.id.et_bank_account);
        bindCodeEt = (EditText) v.findViewById(R.id.et_bind_code);
        getCodeTv = (TextView) v.findViewById(R.id.get_code);
        getCodeTv.setOnClickListener(this);
        submitBt = (Button) v.findViewById(R.id.bt_submit);
        submitBt.setOnClickListener(this);
        alipayCb = (CheckBox) v.findViewById(R.id.cb_alipay);
        alipayCb.setOnCheckedChangeListener(this);
        bankCb = (CheckBox) v.findViewById(R.id.cb_bank);
        bankCb.setOnCheckedChangeListener(this);
        alipayLl = (LinearLayout) v.findViewById(R.id.ll_alipay);
        alipayLl.setOnClickListener(this);
        bankLl = (LinearLayout) v.findViewById(R.id.ll_bank);
        bankLl.setOnClickListener(this);
        bindPhoneTv = (TextView) v.findViewById(R.id.bind_phone);
        String phoneTxt = "您当前绑定的手机号码:" + decodePhone;
        bindPhoneTv.setText(phoneTxt);
    }

    //获取短信验证码
    private void getTextCode() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("phone", userPhone);
        HelperAsyncHttpClient.get(NetConstant.NET_GET_CODE, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
//                            JSONObject jsonObject = response.getJSONObject("data");
//                            String code = jsonObject.getString("code");
                            Toast.makeText(getContext(), "发送成功，请注意接受！", Toast.LENGTH_LONG).show();
                            timeCounter();
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

    private void timeCounter() {
        weakReference = new WeakReference<>(getActivity());
        fragmentActivity = weakReference.get();//使用软引用 防止内存泄漏
        getCodeTv.setClickable(false);
        time = 120;
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (fragmentActivity == null) {
                    return;
                }
                fragmentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (time <= 0) {
                            getCodeTv.setClickable(true);
                            getCodeTv.setText("获取验证码");
                        } else {
                            getCodeTv.setClickable(false);
                            getCodeTv.setText("获取验证码(" + time + ")");
                        }
                        time--;
                    }
                });
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    static class MyHandler extends Handler {

    }

    /**
     * 绑定支付宝账户
     */
    private void bindAlipayAccount() {
        String name = relaNameEt.getText().toString();
        if (!RegexUtil.IsChineseOrEnglish(name)) {
            Toast.makeText(mContext, "输入的姓名不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String idNum = idNumEt.getText().toString();
        if (!RegexUtil.checkIdCard(idNum)) {
            Toast.makeText(mContext, "输入的身份证号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        final String alipayAccount = alipayAccountEt.getText().toString();
        if (alipayAccount.length() == 0) {
            Toast.makeText(mContext, "支付宝账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        String securityCode = bindCodeEt.getText().toString();
        if (securityCode.length() == 0) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams requestParams = new RequestParams();

        requestParams.put("userid", userInfo.getUserId());
        requestParams.put("realname", name);
        requestParams.put("idcard", idNum);
        requestParams.put("alipay", alipayAccount);
        requestParams.put("phone", userPhone);
        requestParams.put("code", securityCode);
        HelperAsyncHttpClient.get(NetConstant.BIND_ALIPAY_ACCOUNT, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_LONG).show();
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
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

    /**
     * 绑定银行账户
     */
    private void bindBankAccount() {
        String name = relaNameEt.getText().toString();
        if (!RegexUtil.IsChineseOrEnglish(name)) {
            Toast.makeText(mContext, "输入的姓名不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String idNum = idNumEt.getText().toString();
        if (!RegexUtil.checkIdCard(idNum)) {
            Toast.makeText(mContext, "输入的身份证号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }
        String binkName = bankNameEt.getText().toString();
        if (binkName.length() == 0) {
            Toast.makeText(mContext, "输入的银行名称不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        String binkAccount = bankAccountEt.getText().toString();
        if (!(binkAccount.length() == 16 || binkAccount.length() == 19)) {
            Toast.makeText(mContext, "输入的银行号码不合法", Toast.LENGTH_SHORT).show();
            return;
        }

        String securityCode = bindCodeEt.getText().toString();
        if (securityCode.length() == 0) {
            Toast.makeText(mContext, "请输入验证码", Toast.LENGTH_SHORT).show();
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
                if (response != null) {
                    try {
                        String state = response.getString("status");
                        if (state.equals("success")) {
                            Toast.makeText(mContext, "绑定成功！", Toast.LENGTH_LONG).show();
                        } else {
                            String data = response.getString("data");
                            Toast.makeText(mContext, data, Toast.LENGTH_LONG).show();
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


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_alipay:
                if (isChecked) {
                    alipayAccountEt.setVisibility(View.VISIBLE);
                    bankAccountEt.setVisibility(View.GONE);
                    bankNameEt.setVisibility(View.GONE);
                }
                break;
            case R.id.cb_bank:
                if (isChecked) {
                    alipayAccountEt.setVisibility(View.GONE);
                    bankAccountEt.setVisibility(View.VISIBLE);
                    bankNameEt.setVisibility(View.VISIBLE);
                }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_alipay:
                alipayCb.setChecked(true);
                bankCb.setChecked(false);
                break;
            case R.id.ll_bank:
                alipayCb.setChecked(false);
                bankCb.setChecked(true);
                break;
            case R.id.get_code:
                getTextCode();
                break;
            case R.id.bt_submit:
                if (alipayCb.isChecked() && !bankCb.isChecked()) {
                    bindAlipayAccount();
                } else if (!alipayCb.isChecked() && bankCb.isChecked()) {
                    bindBankAccount();
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
