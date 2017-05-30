package cn.xcom.helper.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by Administrator on 2016/10/20 0020.
 */

public class VerifyShoppingCodeActivity extends BaseActivity implements View.OnClickListener {
    private EditText verifyCodeEt;
    private Button verifyBtn;
    private UserInfo userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_shopping_code);
        userInfo = new UserInfo(this);
        verifyCodeEt = (EditText) findViewById(R.id.et_verify_code);
        verifyBtn = (Button) findViewById(R.id.btn_verify);
        verifyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verify:
                verityCode();
                break;

        }
    }

    private void verityCode() {
        String code = verifyCodeEt.getText().toString();
        if ("".equals(code)) {
            Toast.makeText(this, "请输入卷码", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams();
        params.put("userid", userInfo.getUserId());
        params.put("code", code);
        HelperAsyncHttpClient.get(NetConstant.VERIFY_SHOPPING_CODE, params,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        if (response != null) {
                            try {
                                String state = response.getString("status");
                                if (state.equals("success")) {
                                    Toast.makeText(VerifyShoppingCodeActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(VerifyShoppingCodeActivity.this, "卷码错误", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });


    }


    public void onBack(View v) {
        finish();
    }
}
